package com.kingpivot.api.dto.memberRank;

public class ApiMemberRankDto {
    private String id;//主键
    private String name;
    private String shortname;
    private Integer orderSeq = 1;
    private String listImage;
    private String faceImage;
    private Double upPoint = 0.0;
    private Double depositeRate = 0.0;

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

    public Double getUpPoint() {
        return upPoint;
    }

    public void setUpPoint(Double upPoint) {
        this.upPoint = upPoint;
    }

    public Double getDepositeRate() {
        return depositeRate;
    }

    public void setDepositeRate(Double depositeRate) {
        this.depositeRate = depositeRate;
    }
}
