package com.kingpivot.base.cart.service.impl;

import com.kingpivot.base.cart.dao.CartDao;
import com.kingpivot.base.cart.model.Cart;
import com.kingpivot.base.cart.service.CartService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("cartService")
public class CartServiceImpl extends BaseServiceImpl<Cart, String> implements CartService {
    @Resource(name = "cartDao")
    private CartDao cartDao;

    @Override
    public BaseDao<Cart, String> getDAO() {
        return this.cartDao;
    }

    @Override
    public String getCartIdByMemberID(String memberID) {
        return cartDao.getCartIdByMemberID(memberID);
    }
}
