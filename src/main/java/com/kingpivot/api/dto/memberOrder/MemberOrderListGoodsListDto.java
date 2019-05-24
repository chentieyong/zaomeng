package com.kingpivot.api.dto.memberOrder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kingpivot.base.goodsShop.model.GoodsShop;
import com.kingpivot.base.objectFeatureItem.model.ObjectFeatureItem;

public class MemberOrderListGoodsListDto {
    private String id;//主键
    private String goodsName;
    private String listImage;
    @JsonIgnore
    private GoodsShop goodsShop;
    private String memberOrderID;//会员订单ID
    private String objectFeatureItemID1; //对象特征选项ID1
    private String objectFeatureItemName1; //对象特征选项Name1
    @JsonIgnore
    private ObjectFeatureItem objectFeatureItem1;
    private Double priceStand = 0.0;//标准价格请
    private Double discountRate = 0.0;//折扣比例
    private Double priceNow = 0.0;//实际价格
    private Double priceReturn = 0.0;//退款价格
    private Integer QTY = 0;//订购数量
    private Double priceTotal = 0.0;//实际总价格
    private Integer isReturn = 0;//是否支持退货
    private Integer status = 1;//状态，1新，2申请退货，3已退货，4已付款，5已发货

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getListImage() {
        return listImage;
    }

    public void setListImage(String listImage) {
        this.listImage = listImage;
    }

    public GoodsShop getGoodsShop() {
        return goodsShop;
    }

    public void setGoodsShop(GoodsShop goodsShop) {
        this.goodsShop = goodsShop;
        if (goodsShop != null) {
            goodsName = goodsShop.getName();
            listImage = goodsShop.getLittleImage();
        }
    }

    public String getMemberOrderID() {
        return memberOrderID;
    }

    public void setMemberOrderID(String memberOrderID) {
        this.memberOrderID = memberOrderID;
    }

    public String getObjectFeatureItemID1() {
        return objectFeatureItemID1;
    }

    public void setObjectFeatureItemID1(String objectFeatureItemID1) {
        this.objectFeatureItemID1 = objectFeatureItemID1;
    }

    public String getObjectFeatureItemName1() {
        return objectFeatureItemName1;
    }

    public void setObjectFeatureItemName1(String objectFeatureItemName1) {
        this.objectFeatureItemName1 = objectFeatureItemName1;
    }

    public ObjectFeatureItem getObjectFeatureItem1() {
        return objectFeatureItem1;
    }

    public void setObjectFeatureItem1(ObjectFeatureItem objectFeatureItem1) {
        this.objectFeatureItem1 = objectFeatureItem1;
        if(objectFeatureItem1!=null){
            this.objectFeatureItemName1 = objectFeatureItem1.getName();
        }
    }

    public Double getPriceStand() {
        return priceStand;
    }

    public void setPriceStand(Double priceStand) {
        this.priceStand = priceStand;
    }

    public Double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Double discountRate) {
        this.discountRate = discountRate;
    }

    public Double getPriceNow() {
        return priceNow;
    }

    public void setPriceNow(Double priceNow) {
        this.priceNow = priceNow;
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
