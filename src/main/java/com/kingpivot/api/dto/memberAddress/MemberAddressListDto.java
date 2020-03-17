package com.kingpivot.api.dto.memberAddress;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kingpivot.base.city.model.City;

public class MemberAddressListDto {
    private String id;//会员地址ID
    private String name;//详细地址
    private String shortName;//地址别名
    private String shengID;//省id
    private String shengName;//省name
    @JsonIgnore
    private City shengCity;
    private String shiID;//市id
    private String shiName;//市name
    @JsonIgnore
    private City shiCity;
    private String xianID;//县id
    private String xianName;//县name
    @JsonIgnore
    private City xianCity;
    private int orderSeq;//排序
    private String contactName;//联系人
    private String phone;//联系电话
    private int isDefault = 0;//是否默认

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

    public String getShengID() {
        return shengID;
    }

    public void setShengID(String shengID) {
        this.shengID = shengID;
    }

    public String getShengName() {
        return shengName;
    }

    public void setShengName(String shengName) {
        this.shengName = shengName;
    }

    public City getShengCity() {
        return shengCity;
    }

    public void setShengCity(City shengCity) {
        this.shengCity = shengCity;
        if (shengCity != null) {
            this.shengName = shengCity.getName();
        }
    }

    public String getShiID() {
        return shiID;
    }

    public void setShiID(String shiID) {
        this.shiID = shiID;
    }

    public String getShiName() {
        return shiName;
    }

    public void setShiName(String shiName) {
        this.shiName = shiName;
    }

    public City getShiCity() {
        return shiCity;
    }

    public void setShiCity(City shiCity) {
        this.shiCity = shiCity;
        if (shiCity != null) {
            this.shiName = shiCity.getName();
        }
    }

    public String getXianID() {
        return xianID;
    }

    public void setXianID(String xianID) {
        this.xianID = xianID;
    }

    public String getXianName() {
        return xianName;
    }

    public void setXianName(String xianName) {
        this.xianName = xianName;
    }

    public City getXianCity() {
        return xianCity;
    }

    public void setXianCity(City xianCity) {
        this.xianCity = xianCity;
        if (xianCity != null) {
            this.xianName = xianCity.getName();
        }
    }

    public int getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(int orderSeq) {
        this.orderSeq = orderSeq;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }
}
