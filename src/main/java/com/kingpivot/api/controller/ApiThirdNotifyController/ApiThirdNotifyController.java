package com.kingpivot.api.controller.ApiThirdNotifyController;

import com.kingpivot.base.config.Config;
import com.kingpivot.base.major.service.MajorService;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.member.service.MemberService;
import com.kingpivot.base.memberCard.service.MemberCardService;
import com.kingpivot.base.memberMajor.service.MemberMajorService;
import com.kingpivot.base.memberOrder.model.MemberOrder;
import com.kingpivot.base.memberOrder.service.MemberOrderService;
import com.kingpivot.base.memberOrderGoods.model.MemberOrderGoods;
import com.kingpivot.base.memberOrderGoods.service.MemberOrderGoodsService;
import com.kingpivot.base.memberPayment.model.MemberPayment;
import com.kingpivot.base.memberPayment.service.MemberPaymentService;
import com.kingpivot.base.memberRecharge.model.MemberRecharge;
import com.kingpivot.base.memberRecharge.service.MemberRechargeService;
import com.kingpivot.base.shopRecharge.service.ShopRechargeService;
import com.kingpivot.common.jms.SendMessageService;
import com.kingpivot.common.jms.dto.memberBalance.MemberBalanceRequest;
import com.kingpivot.common.jms.dto.memberOrder.HbShopBuyGoodsRequest;
import com.kingpivot.common.util.JsonUtil;
import com.kingpivot.common.util.MapUtil;
import com.kingpivot.common.util.XmlUtils;
import com.kingpivot.common.utils.JacksonHelper;
import com.kingpivot.common.utils.NumberUtils;
import com.kingpivot.protocol.ApiBaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


@RequestMapping("/api")
@RestController
@Api(description = "第三方回调管理接口")
public class ApiThirdNotifyController extends ApiBaseController {

    @Autowired
    private MemberPaymentService memberPaymentService;
    @Autowired
    private MemberOrderService memberOrderService;
    @Autowired
    private MemberOrderGoodsService memberOrderGoodsService;
    @Resource
    private SendMessageService sendMessageService;
    @Autowired
    private MemberRechargeService memberRechargeService;
    @Autowired
    private MemberService memberService;

    @ApiOperation(value = "微信订单支付回调", notes = "微信订单支付回调")
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

            MemberOrder memberOrder = memberOrderService.findById(out_trade_no);
            if (null == memberOrder || memberOrder.getStatus() != 1) {
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                return resXml;
            }
            if (null != memberOrder && memberOrder.getStatus() == 4) {
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                return resXml;
            }
            if ("SUCCESS".equals(result_code) && memberOrder.getStatus() == 1) {
                memberOrder.setPayTime(new Timestamp(System.currentTimeMillis()));
                memberOrder.setPayTotal(NumberUtils.keepPrecision(Double.parseDouble(total_fee) / 100, 2));
                memberOrder.setPaySequence(transaction_id);
                memberOrder.setStatus(memberOrder.getSendType() == 1 ? 4 : 8);
                memberOrder.setPayFrom(1);
                memberOrderService.save(memberOrder);

                List<MemberOrderGoods> memberOrderGoodsList = memberOrderGoodsService.getMemberOrderGoodsByMemberOrderID(memberOrder.getId());
                for (MemberOrderGoods memberOrderGoods : memberOrderGoodsList) {
                    memberOrderGoods.setStatus(4);
                    memberOrderGoodsService.save(memberOrderGoods);
                }
                //羊火炉分润
                sendMessageService.sendHbShopBuyGoodsMessage(
                        JacksonHelper.toJson(new HbShopBuyGoodsRequest.Builder()
                                .memberOrderID(memberOrder.getId())
                                .encourageDefineID("000000007bdd5c53017c8e5adcf3004c")
                                .build()));
                //支付成功
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            } else {
                memberOrder.setStatus(-1);
                memberOrderService.save(memberOrder);
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }

        } else {
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        return resXml;

    }

    @ApiOperation(value = "微信充值支付回调", notes = "微信充值支付回调")
    @RequestMapping("/weiXinPayMemberRechargeNotify")
    public String weiXinPayMemberRechargeNotify(HttpServletRequest request) {
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

            MemberRecharge memberRecharge = this.memberRechargeService.findById(out_trade_no);
            if (null == memberRecharge || memberRecharge.getStatus() != 1) {
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                return resXml;
            }
            if (null != memberRecharge && memberRecharge.getStatus() == 2) {
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                return resXml;
            }
            if ("SUCCESS".equals(result_code) && memberRecharge.getStatus() == 1) {
                memberRecharge.setPayTime(new Timestamp(System.currentTimeMillis()));
                memberRecharge.setPayTotal(Double.parseDouble(total_fee) / 100);
                memberRecharge.setPaySequence(transaction_id);
                memberRecharge.setStatus(2);
                memberRecharge.setPaySequence(transaction_id);
                memberRechargeService.save(memberRecharge);

                Member member = memberService.findById(memberRecharge.getMemberID());
                if (member != null) {
                    //处理余额
                    sendMessageService.sendMemberBalance(JacksonHelper.toJson(new MemberBalanceRequest.Builder()
                            .memberID(member.getId())
                            .applicationID(member.getApplicationID())
                            .siteID(member.getSiteID())
                            .operateType(4)
                            .objectDefineID(Config.MEMBERRECHARGE_OBJECTDEFINEID)
                            .objectName(memberRecharge.getName())
                            .objectID(memberRecharge.getId())
                            .amount(new BigDecimal(memberRecharge.getAmount()))
                            .payWayID(memberRecharge.getPaywayID())
                            .description("会员充值")
                            .type(4)
                            .build()));
                }
                //支付成功
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            } else {
                memberRecharge.setStatus(-1);
                memberRechargeService.save(memberRecharge);
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }

        } else {
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        return resXml;

    }

    @ApiOperation(value = "支付宝订单支付回调", notes = "支付宝订单支付回调")
    @RequestMapping(value = "/aliPayMemberOrderNotify")
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

                List<MemberOrder> memberOrderList = memberOrderService.getMemberOrderByMemberPayMentID(memberPayment.getId());
                for (MemberOrder memberOrder : memberOrderList) {
                    memberOrder.setPayTime(memberPayment.getPayTime());
                    memberOrder.setPayTotal(memberOrder.getPriceAfterDiscount());
                    memberOrder.setPaySequence(trade_no);
                    memberOrder.setStatus(6);
                    memberOrder.setPayFrom(2);
                    memberOrderService.save(memberOrder);

                    List<MemberOrderGoods> memberOrderGoodsList = memberOrderGoodsService.getMemberOrderGoodsByMemberOrderID(memberOrder.getId());
                    for (MemberOrderGoods memberOrderGoods : memberOrderGoodsList) {
                        memberOrderGoods.setStatus(4);
                        memberOrderGoodsService.save(memberOrderGoods);
                    }
                    //发送支付成功消息队列
                    sendMessageService.sendZmPaySuccessMessage(memberOrder.getId());
                }

                return "success";
            }
        }
        return "fail";
    }
}
