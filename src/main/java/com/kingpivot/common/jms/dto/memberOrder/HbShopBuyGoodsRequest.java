package com.kingpivot.common.jms.dto.memberOrder;

public class HbShopBuyGoodsRequest {
    private String memberOrderID;

    public static class Builder {
        private String memberOrderID;

        public Builder memberOrderID(String val) {
            memberOrderID = val;
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
    }

    public String getMemberOrderID() {
        return memberOrderID;
    }

    public void setMemberOrderID(String memberOrderID) {
        this.memberOrderID = memberOrderID;
    }
}
