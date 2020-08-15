package com.kingpivot.base.goodsShop.model;


import com.kingpivot.base.category.model.Category;
import com.kingpivot.base.shop.model.Shop;
import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "goodsShop")
public class GoodsShop extends BaseModel<String> {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键
    @Column(length = 36)
    private String companyID;//公司ID
    @Column(length = 36)
    private String shopID;//店铺ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopID", insertable = false, updatable = false)  //不能保存和修改
    private Shop shop;
    @Column(length = 36)
    private String goodsCategoryID;//商品分类id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goodsCategoryID", insertable = false, updatable = false)  //不能保存和修改
    private Category goodsCategory;
    @Column(length = 100)
    private String name;//名称
    @Column(length = 20)
    private String shortName;//简称
    @Column(length = 500)
    private String showName;//展示名称
    @Column()
    private Integer orderSeq;//排序号
    @Column(length = 2000)
    private String description;//描述
    @Column(length = 100)
    private String largerImage;//大图
    @Column(length = 100)
    private String littleImage;//小图
    @Column(length = 100)
    private String videoURL;//视频
    @Column()
    private String unitDescription;//规格说明
    @Column()
    private Integer publishStatus;//上架状态:1=不上架,2=预售,3=在售,4=无货
//    @Column()
//    private Double standPrice = 0.00d;//标准价格
    @Column(length = 20)
    private String priceUnit;//价格单位
    @Column()
    private Double realPrice = 0.0d;//销售价格
    @Column()
    private Integer isBonus = 0;//是否用红包
    @Column()
    private Double pointPayMax = 0.00d;//最大积分抵扣金额
    @Column()
    private int stockNumber = 0;//当前库存
    @Column()
    private Float stockIN = 0.00f;//进货总数
    @Column()
    private int stockOut = 0;//销售总数
    @Column()
    private Float stockReturn = 0.00f;//退货总数
    @Column(length = 20)
    private String salesUnit;//销售单位
    @Column(length = 50)
    private String brandName;//品牌名
    @Column(name = "startingNumber", columnDefinition = "int default 1")
    private Integer startingNumber = 1;//起订量
    @Column(name = "isReturn", columnDefinition = "int default 0")
    private Integer isReturn = 0;//是否支持退货

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
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
    }

    public String getGoodsCategoryID() {
        return goodsCategoryID;
    }

    public void setGoodsCategoryID(String goodsCategoryID) {
        this.goodsCategoryID = goodsCategoryID;
    }

    public Category getGoodsCategory() {
        return goodsCategory;
    }

    public void setGoodsCategory(Category goodsCategory) {
        this.goodsCategory = goodsCategory;
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

    public String getUnitDescription() {
        return unitDescription;
    }

    public void setUnitDescription(String unitDescription) {
        this.unitDescription = unitDescription;
    }

    public Integer getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(Integer publishStatus) {
        this.publishStatus = publishStatus;
    }

//    public Double getStandPrice() {
//        return standPrice;
//    }
//
//    public void setStandPrice(Double standPrice) {
//        this.standPrice = standPrice;
//    }

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
    }

    public Integer getIsBonus() {
        return isBonus;
    }

    public void setIsBonus(Integer isBonus) {
        this.isBonus = isBonus;
    }

    public Double getPointPayMax() {
        return pointPayMax;
    }

    public void setPointPayMax(Double pointPayMax) {
        this.pointPayMax = pointPayMax;
    }

    public int getStockNumber() {
        return stockNumber;
    }

    public void setStockNumber(int stockNumber) {
        this.stockNumber = stockNumber;
    }

    public int getStockOut() {
        return stockOut;
    }

    public void setStockOut(int stockOut) {
        this.stockOut = stockOut;
    }

    public Float getStockIN() {
        return stockIN;
    }

    public void setStockIN(Float stockIN) {
        this.stockIN = stockIN;
    }

    public Float getStockReturn() {
        return stockReturn;
    }

    public void setStockReturn(Float stockReturn) {
        this.stockReturn = stockReturn;
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

    public Integer getStartingNumber() {
        return startingNumber;
    }

    public void setStartingNumber(Integer startingNumber) {
        this.startingNumber = startingNumber;
    }

    public Integer getIsReturn() {
        return isReturn;
    }

    public void setIsReturn(Integer isReturn) {
        this.isReturn = isReturn;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }
}
