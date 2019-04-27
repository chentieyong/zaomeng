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
import com.kingpivot.common.jms.SendMessageService;
import com.kingpivot.common.jms.dto.memberLog.MemberLogRequestBase;
import com.kingpivot.common.util.JsonUtil;
import com.kingpivot.common.util.MapUtil;
import com.kingpivot.common.util.XmlUtils;
import com.kingpivot.common.utils.JacksonHelper;
import com.kingpivot.common.utils.NumberUtils;
import com.kingpivot.common.utils.UserAgentUtil;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Map;


@RequestMapping("/api")
@RestController
@Api(description = "支付管理接口")
public class ApiPayController extends ApiBaseController {

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

    @ApiOperation(value = "app申请订单支付", notes = "app申请订单支付")
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
        String appType = request.getParameter("appType");
        String memberOrderID = request.getParameter("memberOrderID");

        if (StringUtils.isEmpty(payWayID)) {
            return MessagePacket.newFail(MessageHeader.Code.paywayIDNotNull, "paywayID不能为空！");
        }
        PayWay payway = paywayService.findById(payWayID);
        if (payway == null) {
            return MessagePacket.newFail(MessageHeader.Code.paywayIDIsError, "paywayID不正确！");
        }
        if (StringUtils.isEmpty(memberOrderID)) {
            return MessagePacket.newFail(MessageHeader.Code.memberOrderIDIsNull, "memberOrderID不能为空！");
        }

        MemberOrder memberOrder = memberOrderService.findById(memberOrderID);
        if (memberOrder == null) {
            return MessagePacket.newFail(MessageHeader.Code.memberOrderIDIsError, "memberOrderID不正确！");
        }

        if (StringUtils.isEmpty(appType)) {
            return MessagePacket.newFail(MessageHeader.Code.appTypeIsNull, "appType不能为空！终端类型1：Android 2：ios 3：WAP");
        }

        //创建会员支付记录
        MemberPayment memberPayment = new MemberPayment();
        memberPayment.setMemberID(member.getId());
        memberPayment.setName(String.format("%s:订单申请支付", memberOrder.getOrderCode()));
        memberPayment.setDescription(memberPayment.getName());
        memberPayment.setOrderCode(sequenceDefineService.genCode("orderSeq", memberPayment.getId()));
        memberPayment.setObjectDefineID(Config.MEMBERORDER_OBJECTDEFINEID);
        memberPayment.setObjectID(memberOrderID);
        memberPayment.setObjectName(memberOrder.getName());
        memberPayment.setApplyTime(new Timestamp(System.currentTimeMillis()));
        memberPayment.setPaywayID(payWayID);
        memberPayment.setStatus(1);
        memberPaymentService.save(memberPayment);

        Map<String, Object> param = Maps.newHashMap();

        if (payway.getSupportType() == 1) {//app支付宝
            //实例化客户端
            AlipayClient alipayClient = new DefaultAlipayClient(Config.ALIPAY_GATEWAY, payway.getServerPassword(), payway.getPrivateKey(), "json", "utf-8", payway.getCommonKey(), payway.getSecurityType());
            //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
            AlipayTradeAppPayRequest alipayTradeAppPayRequest = new AlipayTradeAppPayRequest();
            //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
            AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
            model.setBody("会员购买商品");
            model.setSubject("会员购买商品");
            model.setOutTradeNo(memberPayment.getId());
            model.setPassbackParams(null);
            model.setTimeoutExpress("30m");
            model.setTotalAmount(String.valueOf(memberOrder.getPriceAfterDiscount()));
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
            payInfo.setBody("会员购买商品");
            payInfo.setOut_trade_no(memberPayment.getId());
            payInfo.setTotal_fee((int) (NumberUtils.keepPrecision(memberOrder.getPriceAfterDiscount().doubleValue(), 2) * 100));
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
        }

        String description = String.format("%sApp申请订单支付", member.getName());

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

    @RequestMapping("/weiXinPayMemberOrderNotify")
    public String weiXinPayMemberOrderNotify(HttpServletRequest request) {
        String inputLine;
        String notityXml = "";
        String resXml = "";
        try {
            while ((inputLine = request.getReader().readLine()) != null) {
                notityXml += inputLine;
            }
            request.getReader().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<weixin-notityXml>>>>>>>>>>>>>>>>>>>>>>>>>：" + notityXml);
        Map<String, String> map = XmlUtils.readStringXmlOut(notityXml);
        if (null != map) {
            String out_trade_no = map.get("out_trade_no");
            String result_code = map.get("result_code");
            String total_fee = map.get("total_fee");
            String transaction_id = map.get("transaction_id");

            MemberPayment memberPayment = memberPaymentService.findById(out_trade_no);
            if (null == memberPayment || memberPayment.getStatus() != 1) {
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                return resXml;
            }
            if (null != memberPayment && memberPayment.getStatus() == 3) {
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                return resXml;
            }
            if ("SUCCESS".equals(result_code) && memberPayment.getStatus() == 1) {
                memberPayment.setPaySequence(transaction_id);
                memberPayment.setPayTime(new Timestamp(System.currentTimeMillis()));
                memberPayment.setStatus(3);
                memberPayment.setAmount(NumberUtils.keepPrecision(Double.parseDouble(total_fee) / 100, 2));
                memberPaymentService.save(memberPayment);

                MemberOrder memberOrder = memberOrderService.findById(memberPayment.getObjectID());
                memberOrder.setPayTime(memberPayment.getPayTime());
                memberOrder.setPayTotal(memberPayment.getAmount());
                memberOrder.setPaySequence(transaction_id);
                memberOrderService.save(memberOrder);

                //支付成功
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            } else {
                memberPayment.setStatus(-1);
                memberPaymentService.save(memberPayment);
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }

        } else {
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        return resXml;

    }

    @RequestMapping("/aliPayMemberOrderNotify")
    @ResponseBody
    public String aliPayMemberOrderNotify(HttpServletRequest request) {
        Map<String, String> paramMap = MapUtil.getParamMapS(request);
        System.out.println(">>>>>>>>>.zhifubao:" + JsonUtil.toJson(paramMap));
        String trade_no = paramMap.get("trade_no");
        String total_amount = paramMap.get("total_amount");
        String out_trade_no = paramMap.get("out_trade_no");
        if (StringUtils.isNotEmpty(out_trade_no)) {
            MemberPayment memberPayment = memberPaymentService.findById(out_trade_no);
            if (null == memberPayment || memberPayment.getStatus() != 1) {
                return "fail";
            }
            if (null != memberPayment && memberPayment.getStatus() == 3) {
                return "success";
            }
            if (memberPayment.getStatus() == 1) {
                memberPayment.setPaySequence(trade_no);
                memberPayment.setPayTime(new Timestamp(System.currentTimeMillis()));
                memberPayment.setStatus(3);
                memberPayment.setAmount(NumberUtils.keepPrecision(Double.parseDouble(total_amount), 2));
                memberPaymentService.save(memberPayment);

                MemberOrder memberOrder = memberOrderService.findById(memberPayment.getObjectID());
                memberOrder.setPayTime(memberPayment.getPayTime());
                memberOrder.setPayTotal(memberPayment.getAmount());
                memberOrder.setPaySequence(trade_no);
                memberOrderService.save(memberOrder);
                return "success";
            }
        }
        return "fail";
    }
}