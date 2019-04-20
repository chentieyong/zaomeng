package com.kingpivot.common;

import com.kingpivot.base.cart.model.Cart;
import com.kingpivot.base.cart.service.CartService;
import com.kingpivot.base.member.model.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by apple on 15/12/17.
 */
@Component
public class KingBase {
    @Autowired
    private CartService cartService;

    private static final Logger logger = LoggerFactory.getLogger(KingBase.class);

    public String insertCart(Member member){
        Cart cart = new Cart();
        cart.setName(String.format("%s的购物车", member.getName()));
        cart.setMemberID(member.getId());
        cartService.save(cart);
        return cart.getId();
    }
}
