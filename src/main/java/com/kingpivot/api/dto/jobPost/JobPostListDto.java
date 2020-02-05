package com.kingpivot.api.dto.jobPost;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kingpivot.base.member.model.Member;

import java.sql.Timestamp;

public class JobPostListDto {
    private String id;//职位需求ID

    private String applicationID;//应用ID

    private String companyName;//公司名称

    private String companyAddress;//公司地址

    private String phone;//电话

    private String memberID;//会员ID

    @JsonIgnore
    private Member member;

    private String memberName;

    private String avatarURL;//头像URL

    private String name;//名称

    private String shortName;//简称

    private String description;//描述

    private Timestamp beginDate;//开始日期

    private Timestamp endDate;//结束日期

    private int needNumber = 1;//需求个数

    private String educationCategoryID;//学历要求

    private String salaryCategoryID;//资金待遇

    private int status = 1;//状态 1新

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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public int getNeedNumber() {
        return needNumber;
    }

    public void setNeedNumber(int needNumber) {
        this.needNumber = needNumber;
    }

    public String getEducationCategoryID() {
        return educationCategoryID;
    }

    public void setEducationCategoryID(String educationCategoryID) {
        this.educationCategoryID = educationCategoryID;
    }

    public String getSalaryCategoryID() {
        return salaryCategoryID;
    }

    public void setSalaryCategoryID(String salaryCategoryID) {
        this.salaryCategoryID = salaryCategoryID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
