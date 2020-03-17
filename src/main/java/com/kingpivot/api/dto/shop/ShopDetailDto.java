package com.kingpivot.api.dto.shop;

public class ShopDetailDto {
    private String id;//主键
    private String companyID;//公司ID
    private String name;//名称
    private String shortName;//简称
    private String description;//详细描述
    private String kingID;//内部唯一编码
    private Integer shopType = 0;//店铺类型: 0= 实体店铺
    private Integer bizStatus = 1;//1=正常2=休业3=停业4=准备中
    private String logoURL;//logoURL
    private String listImage;//列表图
    private String faceImage;//压题图
    private String mapX;//地图X
    private String mapY;//地图Y
    private String headMan;//店长
    private String address;//联系地址
    private String tel;//联系电话
    private String contact;//联系人
    private String contactPhone;//联系人手机

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKingID() {
        return kingID;
    }

    public void setKingID(String kingID) {
        this.kingID = kingID;
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

    public String getListImage() {
        return listImage;
    }

    public void setListImage(String listImage) {
        this.listImage = listImage;
    }

    public String getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }

    public String getMapX() {
        return mapX;
    }

    public void setMapX(String mapX) {
        this.mapX = mapX;
    }

    public String getMapY() {
        return mapY;
    }

    public void setMapY(String mapY) {
        this.mapY = mapY;
    }

    public String getHeadMan() {
        return headMan;
    }

    public void setHeadMan(String headMan) {
        this.headMan = headMan;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
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
}
