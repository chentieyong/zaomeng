package com.kingpivot.common;

import com.kingpivot.base.cart.model.Cart;
import com.kingpivot.base.cart.service.CartService;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.memberPayment.model.MemberPayment;
import com.kingpivot.base.memberPayment.service.MemberPaymentService;
import com.kingpivot.base.memberstatistics.model.MemberStatistics;
import com.kingpivot.base.memberstatistics.service.MemberStatisticsService;
import com.kingpivot.base.sequenceDefine.service.SequenceDefineService;
import com.kingpivot.base.sms.model.SMS;
import com.kingpivot.base.sms.service.SMSService;
import com.kingpivot.common.utils.TimeTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * Created by apple on 15/12/17.
 */
@Component
public class KingBase {
    @Autowired
    private CartService cartService;
    @Autowired
    private SMSService smsService;
    @Autowired
    private MemberPaymentService memberPaymentService;
    @Autowired
    private SequenceDefineService sequenceDefineService;
    @Autowired
    private MemberStatisticsService memberStatisticsService;

    private static final Logger logger = LoggerFactory.getLogger(KingBase.class);

    /**
     * 添加默认购物车
     *
     * @param member
     * @return
     */
    public String insertCart(Member member) {
        Cart cart = new Cart();
        cart.setName(String.format("%s的购物车", member.getName()));
        cart.setMemberID(member.getId());
        cartService.save(cart);
        return cart.getId();
    }

    /**
     * 添加sms记录
     *
     * @param name
     * @param shortName
     * @param content
     * @param phone
     * @param smsWayID
     */
    public void addSms(String name, String shortName, String content,
                       String phone, String smsWayID) {
        SMS sms = new SMS();
        sms.setContent(content);
        sms.setName(name);
        sms.setShortName(shortName);
        sms.setReceiverNumber(phone);
        sms.setSmsWayID(smsWayID);
        sms.setSendDate(new Timestamp(System.currentTimeMillis()));
        smsService.save(sms);
    }

    /**
     * 添加支付日志
     *
     * @param member
     * @param objectDefineID
     * @param amount
     * @return
     */
    public MemberPayment addMemberPayment(Member member, String objectDefineID, double amount) {
        MemberPayment memberPayment = new MemberPayment();
        memberPayment.setName(String.format("%s%s申请支付", member.getName(), TimeTest.getNowDateFormat()));
        memberPayment.setMemberID(member.getId());
        memberPayment.setObjectDefineID(objectDefineID);
        memberPayment.setApplicationID(member.getApplicationID());
        memberPayment.setAmount(amount);
        memberPayment.setApplyTime(new Timestamp(System.currentTimeMillis()));
        memberPayment.setOrderCode(sequenceDefineService.genCode("orderSeq", memberPayment.getId()));
        memberPayment.setStatus(1);
        memberPayment = memberPaymentService.save(memberPayment);
        return memberPayment;
    }

    /**
     * 校验积分是否足够
     *
     * @param member
     * @param number
     * @return
     */
    public boolean pointLess(Member member, int number) {
        int point = memberStatisticsService.getMemberPoint(member.getId());
        if (point < number) {
            return false;
        }
        return true;
    }
}
