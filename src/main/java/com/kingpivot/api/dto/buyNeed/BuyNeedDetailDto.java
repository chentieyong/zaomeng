package com.kingpivot.api.dto.buyNeed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kingpivot.base.member.model.Member;

import java.sql.Timestamp;

public class BuyNeedDetailDto {
    private String id;

    private String memberID;

    @JsonIgnore
    private Member member;

    private String memberName;

    private String avatarURL;//头像URL

    private String name;

    private String shortName;

    private String description;

    private Timestamp beginDate;

    private Timestamp endDate;

    private int beginAmount;

    private int endAmount;

    private String fromWhere;//产地

    private int qty = 1;//数量

    private String priceUnit;//价格单位

    private int status = 1;//1新

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
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

    public Timestamp getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Timestamp beginDate) {
        this.beginDate = beginDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public int getBeginAmount() {
        return beginAmount;
    }

    public void setBeginAmount(int beginAmount) {
        this.beginAmount = beginAmount;
    }

    public int getEndAmount() {
        return endAmount;
    }

    public void setEndAmount(int endAmount) {
        this.endAmount = endAmount;
    }

    public String getFromWhere() {
        return fromWhere;
    }

    public void setFromWhere(String fromWhere) {
        this.fromWhere = fromWhere;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        this.priceUnit = priceUnit;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
