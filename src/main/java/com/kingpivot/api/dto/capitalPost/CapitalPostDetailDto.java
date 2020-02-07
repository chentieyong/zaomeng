package com.kingpivot.api.dto.capitalPost;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.common.utils.TimeTest;

import java.sql.Timestamp;

public class CapitalPostDetailDto {
    private String name;//名称

    private String shortName;//简称

    private String description;//说明

    @JsonIgnore
    private Member member;

    private String memberName;

    private String avatarURL;//头像URL

    private String companyName;

    private String jobName;

    private String memberID;//发布人

    private Timestamp beginDate;//开始日期

    private String beginDateStr;//开始日期Str

    private Timestamp endDate;//

    private String endDateStr;//结束日期

    private int fromType = 1;//资金类型 1股权投资2金融投资3其他投资

    private int stageType = 1;//发展阶段 1种子期 2初创期 3成长期 4稳健期

    private int amountType = 50;//投资资金 1:小余50w 2:50w-100w 3:100w-500w 4:500w以上

    private String industrialName;//投资行业

    private String zoneName;//投资地区

    private int status = 1;//1新

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

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
        if (member != null) {
            this.memberID = member.getId();
            this.memberName = member.getName();
            this.avatarURL = member.getAvatarURL();
            this.companyName = member.getCompanyName();
            this.jobName = member.getJobName();
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

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public Timestamp getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Timestamp beginDate) {
        this.beginDate = beginDate;
        if(beginDate!=null){
            this.beginDateStr = TimeTest.toDateFormat(beginDate);
        }
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
        if(endDate!=null){
            this.endDateStr = TimeTest.toDateFormat(endDate);
        }
    }

    public int getFromType() {
        return fromType;
    }

    public void setFromType(int fromType) {
        this.fromType = fromType;
    }

    public int getStageType() {
        return stageType;
    }

    public void setStageType(int stageType) {
        this.stageType = stageType;
    }

    public int getAmountType() {
        return amountType;
    }

    public void setAmountType(int amountType) {
        this.amountType = amountType;
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

    public String getBeginDateStr() {
        return beginDateStr;
    }

    public void setBeginDateStr(String beginDateStr) {
        this.beginDateStr = beginDateStr;
    }

    public String getEndDateStr() {
        return endDateStr;
    }

    public void setEndDateStr(String endDateStr) {
        this.endDateStr = endDateStr;
    }
}
