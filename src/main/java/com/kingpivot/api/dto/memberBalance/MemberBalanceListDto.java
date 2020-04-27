package com.kingpivot.api.dto.memberBalance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.common.utils.TimeTest;

import java.sql.Timestamp;

public class MemberBalanceListDto {
    private String id;//主键
    private String siteID;//站点id
    private String applicationID;//应用id
    private String name; //名称

    @JsonIgnore
    private Member member;
    private String memberName;
    private String memberID; //会员
    private String avatarURL;//头像URL

    private Integer orderSeq = 1; //序号
    private int operateType; //操作类型-1充值，2购买商品
    private String objectDefineID; //操作对象类型
    private String objectID; //操作对象ID
    private String objectName; //操作对象名称
    private double amount = 0; //操作金额
    private Double balance; //当前余额
    private String description; //描述
    private String verification; //校验码
    private Timestamp operateTime;
    private String operateTimeStr;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSiteID() {
        return siteID;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
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

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
        if (member != null) {
            this.memberName = member.getName();
            this.avatarURL = member.getAvatarURL();
            this.memberID = member.getId();
        }
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public Integer getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(Integer orderSeq) {
        this.orderSeq = orderSeq;
    }

    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    public Timestamp getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Timestamp operateTime) {
        this.operateTime = operateTime;
        if (operateTime != null) {
            this.operateTimeStr = TimeTest.toDateTimeFormat(operateTime);
        }
    }

    public String getOperateTimeStr() {
        return operateTimeStr;
    }

    public void setOperateTimeStr(String operateTimeStr) {
        this.operateTimeStr = operateTimeStr;
    }
}
