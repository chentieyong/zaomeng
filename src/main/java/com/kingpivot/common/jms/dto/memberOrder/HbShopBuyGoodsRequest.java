package com.kingpivot.common.jms.dto.memberOrder;

public class HbShopBuyGoodsRequest {
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

        public HbShopBuyGoodsRequest build() {
            return new HbShopBuyGoodsRequest(this);
        }
    }

    private HbShopBuyGoodsRequest() {
    }

    private HbShopBuyGoodsRequest(Builder builder) {
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
