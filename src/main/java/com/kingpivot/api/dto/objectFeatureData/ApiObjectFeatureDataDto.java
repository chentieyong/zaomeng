package com.kingpivot.api.dto.objectFeatureData;

public class ApiObjectFeatureDataDto {
    private Double showPrice=0d;
    private Double memberPrice=0d;

    public Double getShowPrice() {
        return showPrice;
    }

    public void setShowPrice(Double showPrice) {
        this.showPrice = showPrice;
    }

    public Double getMemberPrice() {
        return memberPrice;
    }

    public void setMemberPrice(Double memberPrice) {
        this.memberPrice = memberPrice;
    }
}
