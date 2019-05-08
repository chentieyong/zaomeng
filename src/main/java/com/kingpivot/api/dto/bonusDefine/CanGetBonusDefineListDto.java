package com.kingpivot.api.dto.bonusDefine;

import com.kingpivot.common.utils.TimeTest;

import java.sql.Timestamp;

public class CanGetBonusDefineListDto {
    private String id;//主键
    private String name;//名称
    private String listImage;
    private String faceImage;
    private double price;
    private int maxNumber = 0;
    private Timestamp startDate;//开始日期
    private String startDateStr;//开始日期Str
    private Timestamp endDate;//结束日期
    private String endDateStr;//开始日期Str

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

    public int getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
        if(startDate!=null){
            startDateStr = TimeTest.toDateFormat(startDate);
        }
    }

    public String getStartDateStr() {
        return startDateStr;
    }

    public void setStartDateStr(String startDateStr) {
        this.startDateStr = startDateStr;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
        if(endDate!=null){
            endDateStr = TimeTest.toDateFormat(endDate);
        }
    }

    public String getEndDateStr() {
        return endDateStr;
    }

    public void setEndDateStr(String endDateStr) {
        this.endDateStr = endDateStr;
    }
}
