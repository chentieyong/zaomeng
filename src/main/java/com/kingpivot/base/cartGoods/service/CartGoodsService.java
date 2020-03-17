package com.kingpivot.base.cartGoods.service;

import com.kingpivot.base.cartGoods.model.CartGoods;
import com.kingpivot.common.service.BaseService;

import java.util.List;

/**
 * Created by Admin on 2016/7/25.
 */
public interface CartGoodsService extends BaseService<CartGoods, String> {
    CartGoods getCartGoodsByCartIDAndObjectFeatureItemID(String cartID, String goodsShopID, String objectFeatureItemID1);

    /**
     * 单店铺版本
     *
     * @param cartID
     * @param isSelect
     * @return
     */
    List<CartGoods> getCartGoodsListByCartID(String cartID, int isSelect);

    /**
     * 多店铺版本
     *
     * @param cartID
     * @param shopID
     * @param isSelect
     * @return
     */
    List<CartGoods> getCartGoodsListByCartID(String cartID, String shopID, int isSelect);

    /**
     * 查询店铺列表-根据cartGoods里面的shopID
     *
     * @param cartID
     * @param isSelected
     * @return
     */
    List<String> getSelectCartGoodsShopList(String cartID, String isSelected);

    /**
     * 购物车总价格
     *
     * @param cartID
     * @return
     */
    Double getPriceTotalByCartID(String cartID);

    /**
     * 会员购物车商品数量
     *
     * @param memberID
     * @return
     */
    int getMemberCartGoodsNum(String memberID);
}
