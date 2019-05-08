package com.kingpivot.base.memberOrderGoods.model;


import com.kingpivot.base.goodsShop.model.GoodsShop;
import com.kingpivot.base.memberOrder.model.MemberOrder;
import com.kingpivot.base.objectFeatureItem.model.ObjectFeatureItem;
import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


/**
 * Created by Administrator on 2016/9/18.
 */
@Entity
@Table(name = "memberOrderGoods")
public class MemberOrderGoods extends BaseModel<String> {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键

    @Column(length = 36)
    private String memberOrderID;//会员订单ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberOrderID", insertable = false, updatable = false)  //不能保存和修改
    private MemberOrder memberOrder;

    @Column(length = 36)
    private String goodsShopID;//商品店铺ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goodsShopID", insertable = false, updatable = false)  //不能保存和修改
    private GoodsShop goodsShop;

    @Column(length = 100)
    private String name;//名称
    @Column(length = 2000)
    private String description;//规格说明
    @Column()
    private Double priceStand = 0.0;//标准价格
    @Column()
    private Double priceStandTotal = 0.0;//标准总价格
    @Column()
    private Double priceNow = 0.0;//实际价格
    @Column()
    private double discountRate = 1d;//折扣比例
    @Column()
    private Double priceReturn = 0.0;//退款价格
    @Column()
    private Integer QTY = 0;//订购数量
    @Column()
    private Double priceTotal = 0.0;//实际总价格
    @Column()
    private Double priceTotalReturn = 0.0;//实际退款价格
    @Column(length = 36)
    private String objectFeatureItemID1; //对象特征选项ID1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "objectFeatureItemID1", updatable = false, insertable = false)
    private ObjectFeatureItem objectFeatureItem1;
    @Column(name = "isReturn", columnDefinition = "int default 0")
    private Integer isReturn = 0;//是否支持退货
    @Column(name = "status", columnDefinition = "int default 1")
    private Integer status = 1;//状态，1新

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberOrderID() {
        return memberOrderID;
    }

    public void setMemberOrderID(String memberOrderID) {
        this.memberOrderID = memberOrderID;
    }

    public MemberOrder getMemberOrder() {
        return memberOrder;
    }

    public void setMemberOrder(MemberOrder memberOrder) {
        this.memberOrder = memberOrder;
    }

    public String getGoodsShopID() {
        return goodsShopID;
    }

    public void setGoodsShopID(String goodsShopID) {
        this.goodsShopID = goodsShopID;
    }

    public GoodsShop getGoodsShop() {
        return goodsShop;
    }

    public void setGoodsShop(GoodsShop goodsShop) {
        this.goodsShop = goodsShop;
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

    public Double getPriceStand() {
        return priceStand;
    }

    public void setPriceStand(Double priceStand) {
        this.priceStand = priceStand;
    }

    public Double getPriceStandTotal() {
        return priceStandTotal;
    }

    public void setPriceStandTotal(Double priceStandTotal) {
        this.priceStandTotal = priceStandTotal;
    }

    public Double getPriceNow() {
        return priceNow;
    }

    public void setPriceNow(Double priceNow) {
        this.priceNow = priceNow;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public Double getPriceReturn() {
        return priceReturn;
    }

    public void setPriceReturn(Double priceReturn) {
        this.priceReturn = priceReturn;
    }

    public Integer getQTY() {
        return QTY;
    }

    public void setQTY(Integer QTY) {
        this.QTY = QTY;
    }

    public Double getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(Double priceTotal) {
        this.priceTotal = priceTotal;
    }

    public Double getPriceTotalReturn() {
        return priceTotalReturn;
    }

    public void setPriceTotalReturn(Double priceTotalReturn) {
        this.priceTotalReturn = priceTotalReturn;
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

    public Integer getIsReturn() {
        return isReturn;
    }

    public void setIsReturn(Integer isReturn) {
        this.isReturn = isReturn;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
