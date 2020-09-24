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
import com.kingpivot.base.memberOrder.model.MemberOrder;
import com.kingpivot.base.memberOrder.service.MemberOrderService;
import com.kingpivot.base.memberPayment.model.MemberPayment;
import com.kingpivot.base.memberPayment.service.MemberPaymentService;
import com.kingpivot.base.memberlog.model.Memberlog;
import com.kingpivot.base.payway.model.PayWay;
import com.kingpivot.base.payway.service.PayWayService;
import com.kingpivot.base.sequenceDefine.service.SequenceDefineService;
import com.kingpivot.base.support.MemberLogDTO;
import com.kingpivot.common.KingBase;
import com.kingpivot.common.jms.SendMessageService;
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
    private SequenceDefineService sequenceDefineService;
    @Autowired
    private MemberPaymentService memberPaymentService;
    @Autowired
    private MemberOrderService memberOrderService;
    @Autowired
    private KingBase kingBase;

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
        String memberPaymentID = request.getParameter("memberPaymentID");
        String openid = request.getParameter("openid");
        String body = request.getParameter("body");

        if (StringUtils.isEmpty(payWayID)) {
            return MessagePacket.newFail(MessageHeader.Code.paywayIDNotNull, "paywayID不能为空！");
        }
        PayWay payway = paywayService.findById(payWayID);
        if (payway == null) {
            return MessagePacket.newFail(MessageHeader.Code.paywayIDIsError, "paywayID不正确！");
        }

        if (StringUtils.isEmpty(memberPaymentID) && StringUtils.isEmpty(memberOrderID)) {
            return MessagePacket.newFail(MessageHeader.Code.illegalParameter, "memberOrderID和memberPaymentID不能同时为空");
        }

        String outTradeNo = "";
        double amount = 0d;

        if (StringUtils.isNotBlank(memberPaymentID)) {
            MemberPayment memberPayment = memberPaymentService.findById(memberPaymentID);
            if (memberPayment == null) {
                return MessagePacket.newFail(MessageHeader.Code.memberOrderIDIsError, "memberPaymentID不正确！");
            }
            memberOrderService.updateMemberOrderByMemberPaymentID(payWayID, memberPaymentID);
            outTradeNo = memberPaymentID;
            amount = memberPayment.getAmount();
        } else if (StringUtils.isNotBlank(memberOrderID)) {
            MemberOrder memberOrder = memberOrderService.findById(memberOrderID);
            if (memberOrder == null) {
                return MessagePacket.newFail(MessageHeader.Code.memberOrderIDIsError, "memberOrderID不正确！");
            }
            if (memberOrder.getStatus() != 1) {
                return MessagePacket.newFail(MessageHeader.Code.statusIsError, "订单状态异常，请重新下单！");
            }
            if (StringUtils.isNotBlank(memberOrder.getMemberPaymentID())) {
                MemberPayment memberPayment = memberPaymentService.findById(memberOrder.getMemberPaymentID());
                if (memberPayment != null && memberPayment.getAmount().doubleValue() == memberOrder.getPriceAfterDiscount()) {
                    memberOrder.setPaywayID(payWayID);
                    memberOrderService.save(memberOrder);
                    memberPaymentID = memberPayment.getId();
                } else {
                    memberPayment = kingBase.addMemberPayment(member, Config.MEMBERORDER_OBJECTDEFINEID, memberOrder.getPriceAfterDiscount());
                    if (memberPayment != null) {
                        memberPaymentID = memberPayment.getId();
                        memberOrder.setMemberPaymentID(memberPaymentID);
                        memberOrder.setPaywayID(payWayID);
                        memberOrderService.save(memberOrder);
                    }
                }
            }
            outTradeNo = memberPaymentID;
            amount = memberOrder.getPriceAfterDiscount();
        }

        if (amount <= 0) {
            return MessagePacket.newFail(MessageHeader.Code.cashBalanceZero, "金额异常，无法支付");
        }

        Map<String, Object> param = Maps.newHashMap();

        if (payway.getSupportType() == 1) {//app支付宝
            //实例化客户端
            AlipayClient alipayClient = new DefaultAlipayClient(Config.ALIPAY_GATEWAY, payway.getServerPassword(),
                    payway.getPrivateKey(), "json", "utf-8",
                    payway.getCommonKey(), payway.getSignType());
            //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
            AlipayTradeAppPayRequest alipayTradeAppPayRequest = new AlipayTradeAppPayRequest();
            //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
            AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
            model.setBody("购买商品");
            model.setSubject("购买商品");
            model.setOutTradeNo(outTradeNo);
            model.setPassbackParams(null);
            model.setTimeoutExpress("30m");
            model.setTotalAmount(String.valueOf(amount));
            model.setProductCode("QUICK_MSECURITY_PAY");
            alipayTradeAppPayRequest.setBizModel(model);
            alipayTradeAppPayRequest.setNotifyUrl(payway.getOrderNotifyURL());
            try {
                //这里和普通的接口调用不同，使用的是sdkExecute
                AlipayTradeAppPayResponse response = alipayClient.sdkExecute(alipayTradeAppPayRequest);
                param.put("dataString", response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
            } catch (AlipayApiException e) {
                e.printStackTrace();
                return MessagePacket.newFail(MessageHeader.Code.sendError, e.getMessage());
            }
        } else if (payway.getSupportType() == 2) {//app微信
            WechatPayInfo payInfo = new WechatPayInfo();
            payInfo.setAppid(payway.getServerPassword());
            payInfo.setMch_id(payway.getPartnerID());
            payInfo.setNonce_str(WechatPayUtils.buildRandom());
            payInfo.setBody("购买商品");
            payInfo.setOut_trade_no(outTradeNo);
            payInfo.setTotal_fee((int) (NumberUtils.keepPrecision(amount, 2) * 100));
            payInfo.setSpbill_create_ip(request.getRemoteAddr());
            payInfo.setNotify_url(payway.getOrderNotifyURL());
            payInfo.setAttach(null);
            payInfo.setTrade_type("APP");
            payInfo.setSign(WechatPayUtils.createSign(MapUtil.beanToMap(payInfo), payway.getPrivateKey()));
            PayOrderResponseInfo payOrderResponseInfo = WechatPayUtils.doGenOrderNo(payInfo);
            if (null != payOrderResponseInfo && "success".equalsIgnoreCase(payOrderResponseInfo.getReturn_code())) {
                param.put("appid", payway.getServerPassword());
                param.put("package", "Sign=WXPay");
                param.put("partnerid", payway.getPartnerID());
                param.put("timestamp", String.format("%s", System.currentTimeMillis()).substring(0, 10));
                param.put("prepayid", payOrderResponseInfo.getPrepay_id());
                param.put("noncestr", payInfo.getNonce_str());
                param.put("sign", WechatPayUtils.createSign(param, payway.getPrivateKey()));
            } else {
                return MessagePacket.newFail(MessageHeader.Code.wecharterror, payOrderResponseInfo.getReturn_msg());
            }
        } else if (payway.getSupportType() == 3) {//微信
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
        }

        String description = String.format("%s申请订单支付", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.APPAPPLYMEMBERORDERPAY.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        return MessagePacket.newSuccess(param, "appApplyMemberOrderPay success!");
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
        double amount = 0d;
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
}
