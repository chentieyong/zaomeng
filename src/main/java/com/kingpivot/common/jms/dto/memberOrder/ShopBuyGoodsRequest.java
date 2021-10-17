package com.kingpivot.common.jms.dto.memberOrder;

public class ShopBuyGoodsRequest {
    private String memberOrderID;

    public static class Builder {
        private String memberOrderID;

        public Builder memberOrderID(String val) {
            memberOrderID = val;
            return this;
        }

        public ShopBuyGoodsRequest build() {
            return new ShopBuyGoodsRequest(this);
        }
    }

    private ShopBuyGoodsRequest() {
    }

    private ShopBuyGoodsRequest(Builder builder) {
        this.memberOrderID = builder.memberOrderID;
    }

    public String getMemberOrderID() {
        return memberOrderID;
    }

    public void setMemberOrderID(String memberOrderID) {
        this.memberOrderID = memberOrderID;
    }
}
