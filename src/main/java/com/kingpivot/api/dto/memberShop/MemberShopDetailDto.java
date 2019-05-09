package com.kingpivot.api.dto.memberShop;

import com.kingpivot.common.utils.TimeTest;

import java.sql.Timestamp;

public class MemberShopDetailDto {
    private String id;//主键
    private String memberID;
    private String shopCategoryID;
    private String applicationID;
    private String name;//名称
    private Integer shopType = 0;//店铺类型: 0= 实体店铺,1= 个人微店,2=个人网店,3=O2O店铺
    private Integer bizStatus = 0;//1=正常2=休业3=停业4=准备中
    private String logoURL;//logoURL
    private String shopFaceImage;//店铺正面照
    private String businessImage;//营业执照
    private String address;//联系地址
    private String contact;//联系人
    private String contactPhone;//联系人手机
    private String contactIdCardFaceImage;//联系人身份证正面
    private String contactIdCardBackImage;//联系人身份证反面
    private Integer verifyStatus;
    private Timestamp verifyDate;
    private String verifyDateStr;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getShopCategoryID() {
        return shopCategoryID;
    }

    public void setShopCategoryID(String shopCategoryID) {
        this.shopCategoryID = shopCategoryID;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getShopType() {
        return shopType;
    }

    public void setShopType(Integer shopType) {
        this.shopType = shopType;
    }

    public Integer getBizStatus() {
        return bizStatus;
    }

    public void setBizStatus(Integer bizStatus) {
        this.bizStatus = bizStatus;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public String getShopFaceImage() {
        return shopFaceImage;
    }

    public void setShopFaceImage(String shopFaceImage) {
        this.shopFaceImage = shopFaceImage;
    }

    public String getBusinessImage() {
        return businessImage;
    }

    public void setBusinessImage(String businessImage) {
        this.businessImage = businessImage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactIdCardFaceImage() {
        return contactIdCardFaceImage;
    }

    public void setContactIdCardFaceImage(String contactIdCardFaceImage) {
        this.contactIdCardFaceImage = contactIdCardFaceImage;
    }

    public String getContactIdCardBackImage() {
        return contactIdCardBackImage;
    }

    public void setContactIdCardBackImage(String contactIdCardBackImage) {
        this.contactIdCardBackImage = contactIdCardBackImage;
    }

    public Integer getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public Timestamp getVerifyDate() {
        return verifyDate;
    }

    public void setVerifyDate(Timestamp verifyDate) {
        this.verifyDate = verifyDate;
        if(verifyDate!=null){
            this.verifyDateStr = TimeTest.toDateTimeFormat(verifyDate);
        }
    }

    public String getVerifyDateStr() {
        return verifyDateStr;
    }

    public void setVerifyDateStr(String verifyDateStr) {
        this.verifyDateStr = verifyDateStr;
    }
}
