package com.kingpivot.base.cartGoods.service.impl;

import com.kingpivot.base.cartGoods.dao.CartGoodsDao;
import com.kingpivot.base.cartGoods.model.CartGoods;
import com.kingpivot.base.cartGoods.service.CartGoodsService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("cartGoodsService")
public class CartGoodsServiceImpl extends BaseServiceImpl<CartGoods, String> implements CartGoodsService {

    @Resource(name = "cartGoodsDao")
    private CartGoodsDao cartGoodsDao;

    @Override
    public BaseDao<CartGoods, String> getDAO() {
        return this.cartGoodsDao;
    }

    @Override
    public CartGoods getCartGoodsByCartIDAndObjectFeatureItemID(String cartID, String goodsShopID, String objectFeatureItemID1) {
        return cartGoodsDao.getCartGoodsByCartIDAndObjectFeatureItemID(cartID, goodsShopID, objectFeatureItemID1);
    }

    @Override
    public List<CartGoods> getCartGoodsListByCartID(String cartID) {
        return cartGoodsDao.getCartGoodsListByCartID(cartID);
    }
}
