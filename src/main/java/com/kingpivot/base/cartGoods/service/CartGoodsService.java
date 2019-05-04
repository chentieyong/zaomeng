package com.kingpivot.base.cartGoods.service;

import com.kingpivot.base.cartGoods.model.CartGoods;
import com.kingpivot.common.service.BaseService;

import java.util.List;

/**
 * Created by Admin on 2016/7/25.
 */
public interface CartGoodsService extends BaseService<CartGoods, String> {
    CartGoods getCartGoodsByCartIDAndObjectFeatureItemID(String cartID, String goodsShopID, String objectFeatureItemID1);

    List<CartGoods> getCartGoodsListByCartID(String cartID);
}
