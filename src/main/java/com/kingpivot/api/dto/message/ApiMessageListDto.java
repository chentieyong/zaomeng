package com.kingpivot.api.dto.message;

import com.kingpivot.common.utils.TimeTest;

import java.sql.Timestamp;

public class ApiMessageListDto {
    private String name;//名称
    private String description;
    private Timestamp sendDate;//发送时间
    private String sendDateStr;//发送时间Str
    private Timestamp readTime;//阅读时间
    private String readTimeStr;//阅读时间Str

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getSendDate() {
        return sendDate;
    }

    public void setSendDate(Timestamp sendDate) {
        this.sendDate = sendDate;
        if(sendDate!=null){
            this.sendDateStr = TimeTest.toDateTimeFormat(sendDate);
        }
    }

    public String getSendDateStr() {
        return sendDateStr;
    }

    public void setSendDateStr(String sendDateStr) {
        this.sendDateStr = sendDateStr;
    }

    public Timestamp getReadTime() {
        return readTime;
    }

    public void setReadTime(Timestamp readTime) {
        this.readTime = readTime;
        if(readTime!=null){
            this.readTimeStr = TimeTest.toDateTimeFormat(readTime);
        }
    }

    public String getReadTimeStr() {
        return readTimeStr;
    }

    public void setReadTimeStr(String readTimeStr) {
        this.readTimeStr = readTimeStr;
    }
}
