package com.kingpivot.api.dto.message;

import com.kingpivot.common.utils.TimeTest;

import java.sql.Timestamp;

public class MessageDetailDto {
    private String id;//主键
    private String name;//名称
    private String shortName;//简称
    private String messageType;//站内信类型
    private String objectDefineID;//对象定义ID
    private String objectID;//对象ID
    private String objectName;
    private String description;
    private String receiverID;//接受人ID
    private Timestamp sendDate;//发送时间
    private String sendDateStr;//发送时间
    private Timestamp readTime;//阅读时间
    private String readTimeStr;//阅读时间
    private int isRead = 0; //是否锁定 0未读，1已读

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

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getObjectDefineID() {
        return objectDefineID;
    }

    public void setObjectDefineID(String objectDefineID) {
        this.objectDefineID = objectDefineID;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
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

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }
}
