package com.kingpivot.common;

import com.kingpivot.base.cart.model.Cart;
import com.kingpivot.base.cart.service.CartService;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.sms.model.SMS;
import com.kingpivot.base.sms.service.SMSService;
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

    private static final Logger logger = LoggerFactory.getLogger(KingBase.class);

    public String insertCart(Member member) {
        Cart cart = new Cart();
        cart.setName(String.format("%s的购物车", member.getName()));
        cart.setMemberID(member.getId());
        cartService.save(cart);
        return cart.getId();
    }

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
}
