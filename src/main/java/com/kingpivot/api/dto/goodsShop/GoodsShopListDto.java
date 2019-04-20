package com.kingpivot.api.dto.goodsShop;

public class GoodsShopListDto {
    private String id;//主键
    private String name;//名称
    private String shortName;//简称
    private String showName;//展示名称
    private Integer orderSeq;//排序号
    private String largerImage;//大图
    private String littleImage;//小图
    private Double standPrice = 0.00d;//标准价格
    private String priceUnit;//价格单位
    private Double realPrice = 0.0d;//销售价格
    private Float stockNumber = 0.00f;//当前库存
    private Float stockOut = 0.00f;//销售总数
    private Double showPrice = 0.0d;//显示价格

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public Integer getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(Integer orderSeq) {
        this.orderSeq = orderSeq;
    }

    public String getLargerImage() {
        return largerImage;
    }

    public void setLargerImage(String largerImage) {
        this.largerImage = largerImage;
    }

    public String getLittleImage() {
        return littleImage;
    }

    public void setLittleImage(String littleImage) {
        this.littleImage = littleImage;
    }

    public Double getStandPrice() {
        return standPrice;
    }

    public void setStandPrice(Double standPrice) {
        this.standPrice = standPrice;
    }

    public String getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        this.priceUnit = priceUnit;
    }

    public Double getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(Double realPrice) {
        this.realPrice = realPrice;
        this.showPrice = realPrice;
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

    public Double getShowPrice() {
        return showPrice;
    }

    public void setShowPrice(Double showPrice) {
        this.showPrice = showPrice;
    }
}
