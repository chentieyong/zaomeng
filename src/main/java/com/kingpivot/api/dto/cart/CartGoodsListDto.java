package com.kingpivot.api.dto.cart;

import java.util.List;

public class CartGoodsListDto {
    private String shopName;
    private List<GoodsListDto> goodsList;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public List<GoodsListDto> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodsListDto> goodsList) {
        this.goodsList = goodsList;
    }
}
