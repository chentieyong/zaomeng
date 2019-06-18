package com.kingpivot.api.controller.ApiThirdNotifyController;

import com.kingpivot.base.goodsShop.model.GoodsShop;
import com.kingpivot.base.goodsShop.service.GoodsShopService;
import com.kingpivot.base.memberOrder.model.MemberOrder;
import com.kingpivot.base.memberOrder.service.MemberOrderService;
import com.kingpivot.base.memberOrderGoods.model.MemberOrderGoods;
import com.kingpivot.base.memberOrderGoods.service.MemberOrderGoodsService;
import com.kingpivot.base.memberPayment.model.MemberPayment;
import com.kingpivot.base.memberPayment.service.MemberPaymentService;
import com.kingpivot.common.jms.SendMessageService;
import com.kingpivot.common.util.JsonUtil;
import com.kingpivot.common.util.MapUtil;
import com.kingpivot.common.util.XmlUtils;
import com.kingpivot.common.utils.NumberUtils;
import com.kingpivot.protocol.ApiBaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    private GoodsShopService goodsShopService;

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

                List<MemberOrder> memberOrderList = memberOrderService.getMemberOrderByMemberPayMentID(memberPayment.getId());
                for (MemberOrder memberOrder : memberOrderList) {
                    memberOrder.setPayTime(memberPayment.getPayTime());
                    memberOrder.setPayTotal(memberOrder.getPriceAfterDiscount());
                    memberOrder.setPaySequence(transaction_id);
                    memberOrder.setStatus(4);
                    memberOrder.setPayFrom(2);
                    memberOrderService.save(memberOrder);

                    List<MemberOrderGoods> memberOrderGoodsList = memberOrderGoodsService.getMemberOrderGoodsByMemberOrderID(memberOrder.getId());
                    for (MemberOrderGoods memberOrderGoods : memberOrderGoodsList) {
                        memberOrderGoods.setStatus(4);
                        memberOrderGoodsService.save(memberOrderGoods);

                        if (memberOrderGoods.getGoodsShop() != null) {
                            GoodsShop goodsShop = memberOrderGoods.getGoodsShop();
                            goodsShop.setStockOut(goodsShop.getStockOut() == null ? memberOrderGoods.getQTY() : memberOrderGoods.getQTY() + goodsShop.getStockOut());
                            goodsShop.setStockNumber(goodsShop.getStockNumber() == null ? 0 : goodsShop.getStockNumber() - memberOrderGoods.getQTY());
                            goodsShopService.save(goodsShop);
                        }
                    }
                    //发送支付成功消息队列
                    sendMessageService.sendZmPaySuccessMessage(memberOrder.getId());
                }
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
                    memberOrder.setStatus(4);
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
