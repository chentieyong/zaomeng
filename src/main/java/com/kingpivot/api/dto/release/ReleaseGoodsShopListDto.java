package com.kingpivot.api.dto.release;

public class ReleaseGoodsShopListDto implements Comparable<ReleaseGoodsShopListDto> {
    private String objectID;
    private String objectName;
    private String listImage;
    private double showPrice = 0.0d;//显示价格
    private Float stockNumber = 0.00f;//当前库存
    private Float stockOut = 0.00f;//销售总数
    private String unitDescription;
    private String objectFeatureItemID1;

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getListImage() {
        return listImage;
    }

    public void setListImage(String listImage) {
        this.listImage = listImage;
    }

    public double getShowPrice() {
        return showPrice;
    }

    public void setShowPrice(double showPrice) {
        this.showPrice = showPrice;
    }

    public Float getStockNumber() {
        return stockNumber;
    }

    public void setStockNumber(Float stockNumber) {
        this.stockNumber = stockNumber;
    }

    public Float getStockOut() {
        return stockOut;
    }

    public void setStockOut(Float stockOut) {
        this.stockOut = stockOut;
    }

    public String getUnitDescription() {
        return unitDescription;
    }

    public void setUnitDescription(String unitDescription) {
        this.unitDescription = unitDescription;
    }

    public String getObjectFeatureItemID1() {
        return objectFeatureItemID1;
    }

    public void setObjectFeatureItemID1(String objectFeatureItemID1) {
        this.objectFeatureItemID1 = objectFeatureItemID1;
    }

    @Override
    public int compareTo(ReleaseGoodsShopListDto o) {
        if (this.getStockOut() > o.getStockOut()) {
            return -1;
        } else if (this.stockOut < o.getStockOut()) {
            return 1;
        } else {
            return 0;
        }
    }
}
