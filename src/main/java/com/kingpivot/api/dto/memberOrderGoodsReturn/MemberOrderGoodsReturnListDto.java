package com.kingpivot.api.dto.memberOrderGoodsReturn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kingpivot.base.goodsShop.model.GoodsShop;
import com.kingpivot.base.objectFeatureItem.model.ObjectFeatureItem;
import com.kingpivot.common.utils.TimeTest;

import java.sql.Timestamp;

public class MemberOrderGoodsReturnListDto {
    private String id;
    private String returnCode;//退货单号
    private String goodsShopName;
    private String littleImage;//小图
    @JsonIgnore
    private GoodsShop goodsShop;
    private Integer qty = 0;//退货数量
    private String objectFeatureItemName1; //对象特征选项Name1
    @JsonIgnore
    private ObjectFeatureItem objectFeatureItem1;
    private Double priceTotalReturn = 0.0;//退款总价格
    private Timestamp applyTime;//申请时间
    private String applyTimeStr;
    private Integer status = 1;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getGoodsShopName() {
        return goodsShopName;
    }

    public void setGoodsShopName(String goodsShopName) {
        this.goodsShopName = goodsShopName;
    }

    public String getLittleImage() {
        return littleImage;
    }

    public void setLittleImage(String littleImage) {
        this.littleImage = littleImage;
    }

    public GoodsShop getGoodsShop() {
        return goodsShop;
    }

    public void setGoodsShop(GoodsShop goodsShop) {
        this.goodsShop = goodsShop;
        if (goodsShop != null) {
            this.goodsShopName = goodsShop.getName();
            this.littleImage = goodsShop.getLittleImage();
        }
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
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
        if (objectFeatureItem1 != null) {
            this.objectFeatureItemName1 = objectFeatureItem1.getName();
        }
    }

    public Double getPriceTotalReturn() {
        return priceTotalReturn;
    }

    public void setPriceTotalReturn(Double priceTotalReturn) {
        this.priceTotalReturn = priceTotalReturn;
    }

    public Timestamp getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Timestamp applyTime) {
        this.applyTime = applyTime;
        if(applyTime!=null){
            this.applyTimeStr = TimeTest.toDateTimeFormat(applyTime);
        }
    }

    public String getApplyTimeStr() {
        return applyTimeStr;
    }

    public void setApplyTimeStr(String applyTimeStr) {
        this.applyTimeStr = applyTimeStr;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
