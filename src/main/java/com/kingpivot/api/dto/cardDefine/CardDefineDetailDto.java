package com.kingpivot.api.dto.cardDefine;

public class CardDefineDetailDto {
    private String id;//主键
    private String companyID;//公司ID
    private String applicationID;
    private String name;
    private String shortname;
    private Integer orderSeq = 1;
    private String listImage;
    private String faceImage;
    private double price = 0d;//申请价格
    private Integer effectiveDays = 0;
    private Integer isMeBuy = 0;

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

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public Integer getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(Integer orderSeq) {
        this.orderSeq = orderSeq;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getEffectiveDays() {
        return effectiveDays;
    }

    public void setEffectiveDays(Integer effectiveDays) {
        this.effectiveDays = effectiveDays;
    }

    public Integer getIsMeBuy() {
        return isMeBuy;
    }

    public void setIsMeBuy(Integer isMeBuy) {
        this.isMeBuy = isMeBuy;
    }
}
