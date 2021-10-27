package com.kingpivot.api.dto.memberCard;

import com.kingpivot.common.utils.TimeTest;

import javax.persistence.Column;
import java.sql.Timestamp;

public class MemberCardListDto {
    private String id;//主键
    private String applicationID;
    private String name;
    private String memberID;
    private String cardDefineID;
    private Timestamp beginTime;
    private String beginTimeStr;
    private Timestamp endTime;
    private String endTimeStr;
    private String listImage;
    private String faceImage;

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

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getCardDefineID() {
        return cardDefineID;
    }

    public void setCardDefineID(String cardDefineID) {
        this.cardDefineID = cardDefineID;
    }

    public Timestamp getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Timestamp beginTime) {
        this.beginTime = beginTime;
        this.beginTimeStr = TimeTest.toDateTimeFormat(beginTime);
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
        this.endTimeStr = TimeTest.toDateTimeFormat(endTime);
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
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
}
