package com.kingpivot.api.dto.goodsShop;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kingpivot.base.shop.model.Shop;

public class GoodsShopDetailDto {
    private String id;//主键
    private String goodsCategoryID;//商品分类id
    private String name;//名称
    private String shortName;//简称
    private String showName;//展示名称
    private String shopID;
    @JsonIgnore
    private Shop shop;
    private String shopName;
    private String description;//描述
    private String largerImage;//大图
    private String littleImage;//小图
    private String videoImage;
    private String videoURL;//视频
    private Double standPrice = 0.00d;//标准价格
    private Double realPrice = 0.0d;//销售价格
    private Float stockNumber = 0.00f;//当前库存
    private Float stockOut = 0.00f;//销售总数
    private String salesUnit;//销售单位
    private String brandName;//品牌名
    private Double showPrice = 0.0d;//显示价格
    private int isCollect = 0;//0未收藏，1已收藏
    private String collectID;
    private String unitDescription;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoodsCategoryID() {
        return goodsCategoryID;
    }

    public void setGoodsCategoryID(String goodsCategoryID) {
        this.goodsCategoryID = goodsCategoryID;
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

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
        if (shop != null) {
            this.shopName = shop.getName();
        }
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public Double getStandPrice() {
        return standPrice;
    }

    public void setStandPrice(Double standPrice) {
        this.standPrice = standPrice;
    }

    public Double getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(Double realPrice) {
        this.realPrice = realPrice;
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

    public String getSalesUnit() {
        return salesUnit;
    }

    public void setSalesUnit(String salesUnit) {
        this.salesUnit = salesUnit;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Double getShowPrice() {
        return showPrice;
    }

    public void setShowPrice(Double showPrice) {
        this.showPrice = showPrice;
    }

    public int getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(int isCollect) {
        this.isCollect = isCollect;
    }

    public String getCollectID() {
        return collectID;
    }

    public void setCollectID(String collectID) {
        this.collectID = collectID;
    }

    public String getUnitDescription() {
        return unitDescription;
    }

    public void setUnitDescription(String unitDescription) {
        this.unitDescription = unitDescription;
    }

    public String getVideoImage() {
        return videoImage;
    }

    public void setVideoImage(String videoImage) {
        this.videoImage = videoImage;
    }
}
