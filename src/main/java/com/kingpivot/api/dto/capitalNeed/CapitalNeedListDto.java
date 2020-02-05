package com.kingpivot.api.dto.capitalNeed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kingpivot.base.member.model.Member;

import java.sql.Timestamp;

public class CapitalNeedListDto {
    private String id;
    private String memberID;//发布人
    @JsonIgnore
    private Member member;
    private String memberName;
    private String avatarURL;//头像URL

    private String name;//名称

    private String shortName;//简称

    private String description;//说明

    private Timestamp beginDate;//开始日期

    private Timestamp endDate;//结束日期

    private int amountType = 1;//融资资金 1:小余50w 2:50w-100w 3:100w-500w 4:500w以上

    private int giveType = 1;//融资方式 1股权投资2金融投资3其他投资

    private int stageType = 1;//发展阶段 1种子期 2初创期 3成长期 4稳健期

    private String industrialName;//所属行业

    private String zoneName;//发布地区

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
        if (member != null) {
            this.memberID = member.getId();
            this.memberName = member.getName();
            this.avatarURL = member.getAvatarURL();
        }
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

    public int getAmountType() {
        return amountType;
    }

    public void setAmountType(int amountType) {
        this.amountType = amountType;
    }

    public int getGiveType() {
        return giveType;
    }

    public void setGiveType(int giveType) {
        this.giveType = giveType;
    }

    public int getStageType() {
        return stageType;
    }

    public void setStageType(int stageType) {
        this.stageType = stageType;
    }

    public String getIndustrialName() {
        return industrialName;
    }

    public void setIndustrialName(String industrialName) {
        this.industrialName = industrialName;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
