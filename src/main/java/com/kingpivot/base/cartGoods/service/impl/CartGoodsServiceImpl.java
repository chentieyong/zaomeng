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
    public List<CartGoods> getCartGoodsListByCartID(String cartID, int isSelect) {
        return cartGoodsDao.getCartGoodsListByCartID(cartID, isSelect);
    }

    @Override
    public List<CartGoods> getCartGoodsListByCartID(String cartID, String shopID, int isSelect) {
        return cartGoodsDao.getCartGoodsListByCartID(cartID, shopID, isSelect);
    }

    @Override
    public List<String> getSelectCartGoodsShopList(String cartID, String isSelected) {
        return cartGoodsDao.getSelectCartGoodsShopList(cartID, isSelected);
    }

    @Override
    public Double getPriceTotalByCartID(String cartID) {
        Double val = cartGoodsDao.getPriceTotalByCartID(cartID);
        if (val == null) {
            return 0d;
        }
        return val.doubleValue();
    }

    @Override
    public int getMemberCartGoodsNum(String memberID) {
        return cartGoodsDao.getMemberCartGoodsNum(memberID);
    }
}
