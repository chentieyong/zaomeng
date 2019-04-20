package com.kingpivot.base.cart.service;

import com.kingpivot.base.cart.model.Cart;
import com.kingpivot.common.service.BaseService;

/**
 * Created by Administrator on 2016/7/25 0025.
 */
public interface CartService extends BaseService<Cart, String> {
    String getCartIdByMemberID(String memberID);

}
