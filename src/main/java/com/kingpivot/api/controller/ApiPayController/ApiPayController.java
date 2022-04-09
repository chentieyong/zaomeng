package com.kingpivot.api.controller.ApiPayController;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.google.common.collect.Maps;
import com.kingpivot.base.config.Config;
import com.kingpivot.base.config.RedisKey;
import com.kingpivot.base.config.UserAgent;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.memberCard.model.MemberCard;
import com.kingpivot.base.memberCard.service.MemberCardService;
import com.kingpivot.base.memberOrder.model.MemberOrder;
import com.kingpivot.base.memberOrder.service.MemberOrderService;
import com.kingpivot.base.memberPayment.model.MemberPayment;
import com.kingpivot.base.memberPayment.service.MemberPaymentService;
import com.kingpivot.base.memberRecharge.model.MemberRecharge;
import com.kingpivot.base.memberRecharge.service.MemberRechargeService;
import com.kingpivot.base.memberlog.model.Memberlog;
import com.kingpivot.base.payway.model.PayWay;
import com.kingpivot.base.payway.service.PayWayService;
import com.kingpivot.base.sequenceDefine.service.SequenceDefineService;
import com.kingpivot.base.shopRecharge.model.ShopRecharge;
import com.kingpivot.base.shopRecharge.service.ShopRechargeService;
import com.kingpivot.base.support.MemberLogDTO;
import com.kingpivot.common.KingBase;
import com.kingpivot.common.jms.SendMessageService;
import com.kingpivot.common.jms.dto.memberBalance.MemberBalanceRequest;
import com.kingpivot.common.jms.dto.memberLog.MemberLogRequestBase;
import com.kingpivot.common.util.MapUtil;
import com.kingpivot.common.utils.*;
import com.kingpivot.common.weixinPay.CodePayOrderResponseInfo;
import com.kingpivot.common.weixinPay.PayOrderResponseInfo;
import com.kingpivot.common.weixinPay.WechatPayInfo;
import com.kingpivot.common.weixinPay.WechatPayUtils;
import com.kingpivot.protocol.ApiBaseController;
import com.kingpivot.protocol.MessageHeader;
import com.kingpivot.protocol.MessagePacket;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;

@RequestMapping("/api")
@RestController
@Api(description = "支付管理接口")
public class ApiPayController extends ApiBaseController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private SendMessageService sendMessageService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private PayWayService paywayService;
    @Autowired
    private ShopRechargeService shopRechargeService;
    @Autowired
    private MemberOrderService memberOrderService;
    @Autowired
    private MemberRechargeService memberRechargeService;

    @ApiOperation(value = "申请订单支付", notes = "申请订单支付")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "payWayID", value = "支付机构id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "appType", value = "app类型", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "memberOrderID", value = "订单id", dataType = "String")})
    @RequestMapping(value = "/appApplyMemberOrderPay")
    public MessagePacket appApplyMemberOrderPay(HttpServletRequest request) throws Exception {
        String sessionID = request.getParameter("sessionID");
        if (StringUtils.isEmpty(sessionID)) {
            return MessagePacket.newFail(MessageHeader.Code.unauth, "sessionID不能为空！");
        }
        if (StringUtils.isEmpty(sessionID)) {
            return MessagePacket.newFail(MessageHeader.Code.unauth, "请先登录");
        }
        Member member = (Member) redisTemplate.opsForValue().get(String.format("%s%s", RedisKey.Key.MEMBER_KEY.key, sessionID));
        if (member == null) {
            return MessagePacket.newFail(MessageHeader.Code.unauth, "请先登录");
        }
        MemberLogDTO memberLogDTO = (MemberLogDTO) redisTemplate.opsForValue().get(String.format("%s%s", RedisKey.Key.MEMBERLOG_KEY.key, sessionID));
        if (memberLogDTO == null) {
            return MessagePacket.newFail(MessageHeader.Code.unauth, "请先登录");
        }

        String payWayID = request.getParameter("payWayID");
        String memberOrderID = request.getParameter("memberOrderID");
        String openid = request.getParameter("openid");
        String body = request.getParameter("body");

        if (StringUtils.isEmpty(payWayID)) {
            return MessagePacket.newFail(MessageHeader.Code.paywayIDNotNull, "paywayID不能为空！");
        }
        PayWay payway = paywayService.findById(payWayID);
        if (payway == null) {
            return MessagePacket.newFail(MessageHeader.Code.paywayIDIsError, "paywayID不正确！");
        }

        if (StringUtils.isEmpty(memberOrderID)) {
            return MessagePacket.newFail(MessageHeader.Code.illegalParameter, "memberOrderID不能为空");
        }

        MemberOrder memberOrder = memberOrderService.findById(memberOrderID);
        if (memberOrder == null) {
            return MessagePacket.newFail(MessageHeader.Code.memberOrderIDIsError, "memberOrderID不正确！");
        }
        if (memberOrder.getStatus() != 1) {
            return MessagePacket.newFail(MessageHeader.Code.statusIsError, "订单状态异常，请重新下单！");
        }
        memberOrder.setPaywayID(payWayID);
        memberOrderService.save(memberOrder);

        if (memberOrder.getPriceAfterDiscount() <= 0) {
            return MessagePacket.newFail(MessageHeader.Code.cashBalanceZero, "金额异常，无法支付");
        }

        Map<String, Object> param = Maps.newHashMap();

        WechatPayInfo payInfo = new WechatPayInfo();
        payInfo.setAppid(payway.getServerPassword());
        payInfo.setMch_id(payway.getPartnerID());
        payInfo.setNonce_str(WechatPayUtils.buildRandom());
        payInfo.setBody(body);
        payInfo.setAttach(body);
        Double total_fee = NumberUtils.keepPrecision((memberOrder.getPriceAfterDiscount() + memberOrder.getSendPrice()) * 100d, 2);
        payInfo.setTotal_fee(total_fee.intValue());
        payInfo.setSpbill_create_ip(WebUtil.getRemortIP(request));
        payInfo.setOut_trade_no(memberOrder.getId());
        payInfo.setNotify_url(payway.getOrderNotifyURL());
        payInfo.setTrade_type("JSAPI");
        payInfo.setOpenid(openid);
        payInfo.setSign(WechatPayUtils.createSign(MapUtil.beanToMap(payInfo), payway.getPrivateKey()));
        String result = "";
        try {
            result = WechatPayUtils.doGenOrder(XStreamUtil.getXstream(WechatPayInfo.class).toXML(payInfo));
            logger.info("微信报文={}", result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PayOrderResponseInfo payOrderResponseInfo = (PayOrderResponseInfo) XStreamUtil.getXstream(PayOrderResponseInfo.class).fromXML(result);

        if (payOrderResponseInfo != null && !payOrderResponseInfo.getResult_code().equals("SUCCESS")) {
            return MessagePacket.newFail(MessageHeader.Code.illegalParameter, payOrderResponseInfo.getErr_code_des());
        }

        param.put("appId", payway.getServerPassword());
        param.put("timeStamp", String.format("%s", System.currentTimeMillis()).substring(0, 10));
        param.put("nonceStr", WechatPayUtils.buildRandom());
        param.put("package", "prepay_id=" + payOrderResponseInfo.getPrepay_id());
        param.put("signType", "MD5");
        String sign = WechatPayUtils.createSign(param, payway.getPrivateKey());
        param.put("packageValue", "prepay_id=" + payOrderResponseInfo.getPrepay_id());
        param.put("paySign", sign);
        String userAgent = request.getHeader("user-agent");
        char agent = userAgent.charAt(userAgent.indexOf("MicroMessenger") + 15);
        param.put("agent", agent);

        String description = String.format("%s申请订单支付", member.getName());

        UserAgent userAgents = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgents == null ? null : userAgents.getBrowserType())
                .operateType(Memberlog.MemberOperateType.APPAPPLYMEMBERORDERPAY.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        return MessagePacket.newSuccess(param, "appApplyMemberOrderPay success!");
    }

    @ApiOperation(value = "申请会员充值支付", notes = "申请会员充值支付")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "payWayID", value = "支付机构id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "appType", value = "app类型", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "memberOrderID", value = "订单id", dataType = "String")})
    @RequestMapping(value = "/appApplyMemberRechargePay")
    public MessagePacket appApplyMemberRechargePay(HttpServletRequest request) throws Exception {
        String sessionID = request.getParameter("sessionID");
        if (StringUtils.isEmpty(sessionID)) {
            return MessagePacket.newFail(MessageHeader.Code.unauth, "sessionID不能为空！");
        }
        if (StringUtils.isEmpty(sessionID)) {
            return MessagePacket.newFail(MessageHeader.Code.unauth, "请先登录");
        }
        Member member = (Member) redisTemplate.opsForValue().get(String.format("%s%s", RedisKey.Key.MEMBER_KEY.key, sessionID));
        if (member == null) {
            return MessagePacket.newFail(MessageHeader.Code.unauth, "请先登录");
        }
        MemberLogDTO memberLogDTO = (MemberLogDTO) redisTemplate.opsForValue().get(String.format("%s%s", RedisKey.Key.MEMBERLOG_KEY.key, sessionID));
        if (memberLogDTO == null) {
            return MessagePacket.newFail(MessageHeader.Code.unauth, "请先登录");
        }

        String payWayID = request.getParameter("payWayID");
        String memberRechargeID = request.getParameter("memberRechargeID");
        String openid = request.getParameter("openid");
        String body = request.getParameter("body");

        if (StringUtils.isEmpty(payWayID)) {
            return MessagePacket.newFail(MessageHeader.Code.paywayIDNotNull, "paywayID不能为空！");
        }
        PayWay payway = paywayService.findById(payWayID);
        if (payway == null) {
            return MessagePacket.newFail(MessageHeader.Code.paywayIDIsError, "paywayID不正确！");
        }

        if (StringUtils.isEmpty(memberRechargeID)) {
            return MessagePacket.newFail(MessageHeader.Code.illegalParameter, "充值记录不能为空");
        }

        MemberRecharge memberRecharge = memberRechargeService.findById(memberRechargeID);
        if (memberRecharge == null) {
            return MessagePacket.newFail(MessageHeader.Code.memberOrderIDIsError, "充值记录不存在");
        }
        if (memberRecharge.getStatus() != 1) {
            return MessagePacket.newFail(MessageHeader.Code.statusIsError, "充值状态异常，请重新下单！");
        }
        memberRecharge.setPaywayID(payWayID);
        memberRechargeService.save(memberRecharge);

        Map<String, Object> param = Maps.newHashMap();

        WechatPayInfo payInfo = new WechatPayInfo();
        payInfo.setAppid(payway.getServerPassword());
        payInfo.setMch_id(payway.getPartnerID());
        payInfo.setNonce_str(WechatPayUtils.buildRandom());
        payInfo.setBody(body);
        payInfo.setAttach(body);
        Double total_fee = NumberUtils.keepPrecision(memberRecharge.getAmount() * 100d, 2);
        payInfo.setTotal_fee(total_fee.intValue());
        payInfo.setSpbill_create_ip(WebUtil.getRemortIP(request));
        payInfo.setOut_trade_no(memberRecharge.getMemberID());
        payInfo.setNotify_url(payway.getRechargeNotifyURL());
        payInfo.setTrade_type("JSAPI");
        payInfo.setOpenid(openid);
        payInfo.setSign(WechatPayUtils.createSign(MapUtil.beanToMap(payInfo), payway.getPrivateKey()));
        String result = "";
        try {
            result = WechatPayUtils.doGenOrder(XStreamUtil.getXstream(WechatPayInfo.class).toXML(payInfo));
            logger.info("微信报文={}", result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PayOrderResponseInfo payOrderResponseInfo = (PayOrderResponseInfo) XStreamUtil.getXstream(PayOrderResponseInfo.class).fromXML(result);

        if (payOrderResponseInfo != null && !payOrderResponseInfo.getResult_code().equals("SUCCESS")) {
            return MessagePacket.newFail(MessageHeader.Code.illegalParameter, payOrderResponseInfo.getErr_code_des());
        }

        param.put("appId", payway.getServerPassword());
        param.put("timeStamp", String.format("%s", System.currentTimeMillis()).substring(0, 10));
        param.put("nonceStr", WechatPayUtils.buildRandom());
        param.put("package", "prepay_id=" + payOrderResponseInfo.getPrepay_id());
        param.put("signType", "MD5");
        String sign = WechatPayUtils.createSign(param, payway.getPrivateKey());
        param.put("packageValue", "prepay_id=" + payOrderResponseInfo.getPrepay_id());
        param.put("paySign", sign);
        String userAgent = request.getHeader("user-agent");
        char agent = userAgent.charAt(userAgent.indexOf("MicroMessenger") + 15);
        param.put("agent", agent);

        String description = String.format("%s申请充值支付", member.getName());

        UserAgent userAgents = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgents == null ? null : userAgents.getBrowserType())
                .operateType(Memberlog.MemberOperateType.APPLYMEMBERRECHARGEPAY.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        return MessagePacket.newSuccess(param, "appApplyMemberRechargePay success!");
    }

    @ApiOperation(value = "获取微信支付二维码url", notes = "获取微信支付二维码url")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "payWayID", value = "支付机构id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "body", value = "说明", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "memberPaymentID", value = "会员支付id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "memberOrderID", value = "订单id", dataType = "String")})
    @RequestMapping(value = "/getWeixinQrcodePayUrl")
    public MessagePacket getWeixinQrcodePayUrl(HttpServletRequest request) {
        String payWayID = request.getParameter("payWayID");
        String body = request.getParameter("body");
        if (StringUtils.isEmpty(payWayID)) {
            return MessagePacket.newFail(MessageHeader.Code.paywayIDNotNull, "paywayID不能为空！");
        }
        PayWay payway = paywayService.findById(payWayID);
        if (payway == null) {
            return MessagePacket.newFail(MessageHeader.Code.paywayIDIsError, "paywayID不正确！");
        }
        String outTradeNo = request.getParameter("outTradeNo");
        if (StringUtils.isEmpty(outTradeNo)) {
            return MessagePacket.newFail(MessageHeader.Code.illegalParameter, "外部流水号为空！");
        }

        ShopRecharge shopRecharge = shopRechargeService.findById(outTradeNo);
        if (shopRecharge == null || shopRecharge.getIsValid() == 0) {
            return MessagePacket.newFail(MessageHeader.Code.illegalParameter, "外部流水号异常！");
        }

        double amount = shopRecharge.getRechargeAmount();
        if (amount <= 0) {
            return MessagePacket.newFail(MessageHeader.Code.cashBalanceZero, "金额异常，无法支付");
        }
        WechatPayInfo payInfo = new WechatPayInfo();
        payInfo.setAppid(payway.getServerPassword());
        payInfo.setMch_id(payway.getPartnerID());
        payInfo.setNonce_str(WechatPayUtils.buildRandom());
        payInfo.setBody(body);
        payInfo.setAttach(body);
        Double total_fee = NumberUtils.keepPrecision(amount * 100d, 2);
        payInfo.setTotal_fee(total_fee.intValue());
        payInfo.setSpbill_create_ip(WebUtil.getRemortIP(request));
        payInfo.setOut_trade_no(outTradeNo);
        payInfo.setNotify_url(payway.getOrderNotifyURL());
        payInfo.setTrade_type("NATIVE");
        payInfo.setSign(WechatPayUtils.createSign(MapUtil.beanToMap(payInfo), payway.getPrivateKey()));
        String result = "";
        try {
            result = WechatPayUtils.doGenOrder(XStreamUtil.getXstream(WechatPayInfo.class).toXML(payInfo));
            logger.info("微信报文={}", result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        CodePayOrderResponseInfo responseInfo = (CodePayOrderResponseInfo) XStreamUtil.getXstream(CodePayOrderResponseInfo.class).fromXML(result);
        if (responseInfo != null && !responseInfo.getResult_code().equals("SUCCESS")) {
            return MessagePacket.newFail(MessageHeader.Code.illegalParameter, responseInfo.getErr_code_des());
        }
        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("code_url", responseInfo.getCode_url());
        return MessagePacket.newSuccess(rsMap, "getWeixinQrcodePayUrl success!");
    }

    @ApiOperation(value = "余额支付订单", notes = "余额支付订单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "payWayID", value = "支付机构id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "memberOrderID", value = "订单id", dataType = "String")})
    @RequestMapping(value = "/balancePayMemberOrder")
    public MessagePacket balancePayMemberOrder(HttpServletRequest request) throws Exception {
        String sessionID = request.getParameter("sessionID");
        if (StringUtils.isEmpty(sessionID)) {
            return MessagePacket.newFail(MessageHeader.Code.unauth, "sessionID不能为空！");
        }
        if (StringUtils.isEmpty(sessionID)) {
            return MessagePacket.newFail(MessageHeader.Code.unauth, "请先登录");
        }
        Member member = (Member) redisTemplate.opsForValue().get(String.format("%s%s", RedisKey.Key.MEMBER_KEY.key, sessionID));
        if (member == null) {
            return MessagePacket.newFail(MessageHeader.Code.unauth, "请先登录");
        }
        MemberLogDTO memberLogDTO = (MemberLogDTO) redisTemplate.opsForValue().get(String.format("%s%s", RedisKey.Key.MEMBERLOG_KEY.key, sessionID));
        if (memberLogDTO == null) {
            return MessagePacket.newFail(MessageHeader.Code.unauth, "请先登录");
        }

        String payWayID = request.getParameter("payWayID");
        String memberOrderID = request.getParameter("memberOrderID");
        String encourageDefineID = request.getParameter("encourageDefineID");

        if (StringUtils.isEmpty(payWayID)) {
            return MessagePacket.newFail(MessageHeader.Code.paywayIDNotNull, "paywayID不能为空！");
        }
        PayWay payway = paywayService.findById(payWayID);
        if (payway == null) {
            return MessagePacket.newFail(MessageHeader.Code.paywayIDIsError, "paywayID不正确！");
        }

        if (StringUtils.isEmpty(memberOrderID)) {
            return MessagePacket.newFail(MessageHeader.Code.illegalParameter, "memberOrderID不能为空");
        }

        MemberOrder memberOrder = memberOrderService.findById(memberOrderID);
        if (memberOrder == null) {
            return MessagePacket.newFail(MessageHeader.Code.memberOrderIDIsError, "memberOrderID不正确！");
        }
        if (memberOrder.getStatus() != 1) {
            return MessagePacket.newFail(MessageHeader.Code.statusIsError, "订单状态异常，请重新下单！");
        }

        if (memberOrder.getPriceAfterDiscount() <= 0) {
            return MessagePacket.newFail(MessageHeader.Code.cashBalanceZero, "金额异常，无法支付");
        }

        //调用队列余额支付
        sendMessageService.sendMemberBalance(JacksonHelper.toJson(new MemberBalanceRequest.Builder()
                .memberID(member.getId())
                .applicationID(member.getApplicationID())
                .siteID(member.getSiteID())
                .operateType(3)
                .objectDefineID(Config.MEMBERORDER_OBJECTDEFINEID)
                .objectName(memberOrder.getName())
                .objectID(memberOrder.getId())
                .amount(new BigDecimal(memberOrder.getPriceAfterDiscount() + memberOrder.getSendPrice()))
                .payWayID(payWayID)
                .encourageDefineID(encourageDefineID)
                .description("余额支付购买商品")
                .type(3)
                .build()));

        String description = String.format("%s申请订单支付", member.getName());
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.APPAPPLYMEMBERORDERPAY.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        return MessagePacket.newSuccess("balancePayMemberOrder success!");
    }
}
