package com.kingpivot.common.jms.dto.memberOrder;

public class ShopBuyGoodsRequest {
    private String memberOrderID;
    private String encourageDefineID;

    public static class Builder {
        private String memberOrderID;
        private String encourageDefineID;

        public Builder memberOrderID(String val) {
            memberOrderID = val;
            return this;
        }

        public Builder encourageDefineID(String val) {
            encourageDefineID = val;
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
        this.encourageDefineID = builder.encourageDefineID;
    }

    public String getMemberOrderID() {
        return memberOrderID;
    }

    public void setMemberOrderID(String memberOrderID) {
        this.memberOrderID = memberOrderID;
    }

    public String getEncourageDefineID() {
        return encourageDefineID;
    }

    public void setEncourageDefineID(String encourageDefineID) {
        this.encourageDefineID = encourageDefineID;
    }
}
