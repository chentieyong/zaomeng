package com.kingpivot.base.cartGoods.model;

import com.kingpivot.base.objectFeatureItem.model.ObjectFeatureItem;
import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "cartGoods")
public class CartGoods extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键

    @Column(length = 100)
    private String name;

    @Column(length = 2000)
    private String description;//描述

    @Column(length = 36)
    private String cartID;

    @Column(length = 36)
    private String goodsShopID;//商品店铺ID

    @Column()
    private Double standPrice = 0.00d;//标准价格

    @Column()
    private Double standPriceTotal = 0.00d;//标准价格

    @Column()
    private double discountRate = 1d;//折扣比例

    @Column()
    private Integer qty = 0;//当前商品数量

    @Column()
    private double priceNow = 0.00d;//实际价格

    @Column()
    private double priceTotal = 0.00d;//实际总价格

    @Column(length = 36)
    private String objectFeatureItemID1; //对象特征选项ID1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "objectFeatureItemID1", updatable = false, insertable = false)
    private ObjectFeatureItem objectFeatureItem1;

    @Column(name = "isSelected", columnDefinition = "int default 1")
    private Integer isSelected = 1;//是否选中: 1是 2否

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCartID() {
        return cartID;
    }

    public void setCartID(String cartID) {
        this.cartID = cartID;
    }

    public String getGoodsShopID() {
        return goodsShopID;
    }

    public void setGoodsShopID(String goodsShopID) {
        this.goodsShopID = goodsShopID;
    }

    public Double getStandPrice() {
        return standPrice;
    }

    public void setStandPrice(Double standPrice) {
        this.standPrice = standPrice;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public double getPriceNow() {
        return priceNow;
    }

    public void setPriceNow(double priceNow) {
        this.priceNow = priceNow;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public double getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(double priceTotal) {
        this.priceTotal = priceTotal;
    }

    public String getObjectFeatureItemID1() {
        return objectFeatureItemID1;
    }

    public void setObjectFeatureItemID1(String objectFeatureItemID1) {
        this.objectFeatureItemID1 = objectFeatureItemID1;
    }

    public ObjectFeatureItem getObjectFeatureItem1() {
        return objectFeatureItem1;
    }

    public void setObjectFeatureItem1(ObjectFeatureItem objectFeatureItem1) {
        this.objectFeatureItem1 = objectFeatureItem1;
    }

    public Integer getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Integer isSelected) {
        this.isSelected = isSelected;
    }

    public Double getStandPriceTotal() {
        return standPriceTotal;
    }

    public void setStandPriceTotal(Double standPriceTotal) {
        this.standPriceTotal = standPriceTotal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
