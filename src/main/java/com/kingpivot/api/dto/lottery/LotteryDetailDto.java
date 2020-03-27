package com.kingpivot.api.dto.lottery;

import com.kingpivot.common.utils.TimeTest;

import java.sql.Timestamp;

public class LotteryDetailDto {
    private String id;//职位需求ID
    private String applicationID;//应用ID
    private String name;//名称
    private String shortName;//简称
    private int orderSeq = 1;//排序号
    private String description;//描述
    private int doType = 1;//抽奖类型 1集中开奖 2立即开奖
    private int usePoint = 1;//消耗积分
    private String listImage;//列表图
    private String faceImage;//压题图
    private Timestamp beginTime;//开始时间
    private String beginTimeStr;//开始时间
    private Timestamp endTime;//结束时间
    private String endTimeStr;//结束时间
    private Timestamp openTime;//开奖时间
    private String openTimeStr;//开奖时间
    private Timestamp myJoinTime;//我的参加时间
    private String myJoinTimeStr;//我的参加时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(int orderSeq) {
        this.orderSeq = orderSeq;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDoType() {
        return doType;
    }

    public void setDoType(int doType) {
        this.doType = doType;
    }

    public int getUsePoint() {
        return usePoint;
    }

    public void setUsePoint(int usePoint) {
        this.usePoint = usePoint;
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

    public Timestamp getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Timestamp beginTime) {
        this.beginTime = beginTime;
        if (beginTime != null) {
            this.beginTimeStr = TimeTest.toDateTimeFormat(beginTime);
        }
    }

    public String getBeginTimeStr() {
        return beginTimeStr;
    }

    public void setBeginTimeStr(String beginTimeStr) {
        this.beginTimeStr = beginTimeStr;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
        if (endTime != null) {
            this.endTimeStr = TimeTest.toDateTimeFormat(endTime);
        }
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public Timestamp getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Timestamp openTime) {
        this.openTime = openTime;
        if (openTime != null) {
            this.openTimeStr = TimeTest.toDateTimeFormat(openTime);
        }
    }

    public String getOpenTimeStr() {
        return openTimeStr;
    }

    public void setOpenTimeStr(String openTimeStr) {
        this.openTimeStr = openTimeStr;
    }

    public Timestamp getMyJoinTime() {
        return myJoinTime;
    }

    public void setMyJoinTime(Timestamp myJoinTime) {
        this.myJoinTime = myJoinTime;
        if (myJoinTime != null) {
            this.myJoinTimeStr = TimeTest.toDateTimeFormat(myJoinTime);
        }
    }

    public String getMyJoinTimeStr() {
        return myJoinTimeStr;
    }

    public void setMyJoinTimeStr(String myJoinTimeStr) {
        this.myJoinTimeStr = myJoinTimeStr;
    }
}
