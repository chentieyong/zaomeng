package com.kingpivot.api.dto.major;

public class MajorListDto {
    private String id;//主键
    private String companyID;//公司ID
    private String name;
    private String shortName;
    private Integer orderSeq = 1;
    private String listImage;
    private String faceImage;
    private int applyType;//1注册赠送，2会员申请，3后台赠送
    private int myVerifyStatus;
    private String myApplyTimeStr;
    private int upgradeNumber;//升级个数
    private int alreadyUpgradeNumber;//已经升级个数
    private int maxFollows;//最多粉丝数量
    private double price;//申请价格

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

    public int getApplyType() {
        return applyType;
    }

    public void setApplyType(int applyType) {
        this.applyType = applyType;
    }

    public int getMyVerifyStatus() {
        return myVerifyStatus;
    }

    public void setMyVerifyStatus(int myVerifyStatus) {
        this.myVerifyStatus = myVerifyStatus;
    }

    public String getMyApplyTimeStr() {
        return myApplyTimeStr;
    }

    public void setMyApplyTimeStr(String myApplyTimeStr) {
        this.myApplyTimeStr = myApplyTimeStr;
    }

    public int getUpgradeNumber() {
        return upgradeNumber;
    }

    public void setUpgradeNumber(int upgradeNumber) {
        this.upgradeNumber = upgradeNumber;
    }

    public int getAlreadyUpgradeNumber() {
        return alreadyUpgradeNumber;
    }

    public void setAlreadyUpgradeNumber(int alreadyUpgradeNumber) {
        this.alreadyUpgradeNumber = alreadyUpgradeNumber;
    }

    public int getMaxFollows() {
        return maxFollows;
    }

    public void setMaxFollows(int maxFollows) {
        this.maxFollows = maxFollows;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
