package com.kingpivot.api.dto.jobPost;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kingpivot.base.category.model.Category;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.common.utils.TimeTest;

import java.sql.Timestamp;

public class JobPostDetailDto {
    private String id;//职位需求ID

    private String applicationID;//应用ID

    private String companyName;//公司名称

    private String companyAddress;//公司地址

    private String jobCategory;//工作性质

    private String phone;//电话

    private String memberID;//会员ID

    @JsonIgnore
    private Member member;

    private String memberName;

    private String avatarURL;//头像URL

    private String memberCompanyName;

    private String memberJobName;

    private String name;//名称

    private String shortName;//简称

    private String description;//描述

    private Timestamp beginDate;//开始日期

    private String beginDateStr;//开始日期

    private Timestamp endDate;//结束日期

    private String endDateStr;//结束日期

    private int needNumber = 1;//需求个数

    private String educationCategoryID;//学历要求

    private String educationCategoryName;//学历要求

    @JsonIgnore
    private Category educationCategory;

    private String salaryCategoryID;//资金待遇

    @JsonIgnore
    private Category salaryCategory;

    private String salaryCategoryName;//资金待遇

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
            this.memberCompanyName = member.getCompanyName();
            this.memberJobName = member.getJobName();
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
        if (beginDate != null) {
            this.beginDateStr = TimeTest.toDateFormat(beginDate);
        }
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
        if (endDate != null) {
            this.endDateStr = TimeTest.toDateFormat(endDate);
        }
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

    public String getMemberCompanyName() {
        return memberCompanyName;
    }

    public void setMemberCompanyName(String memberCompanyName) {
        this.memberCompanyName = memberCompanyName;
    }

    public String getMemberJobName() {
        return memberJobName;
    }

    public void setMemberJobName(String memberJobName) {
        this.memberJobName = memberJobName;
    }

    public String getJobCategory() {
        return jobCategory;
    }

    public void setJobCategory(String jobCategory) {
        this.jobCategory = jobCategory;
    }

    public String getEducationCategoryName() {
        return educationCategoryName;
    }

    public void setEducationCategoryName(String educationCategoryName) {
        this.educationCategoryName = educationCategoryName;
    }

    public Category getEducationCategory() {
        return educationCategory;
    }

    public void setEducationCategory(Category educationCategory) {
        this.educationCategory = educationCategory;
        if (educationCategory != null) {
            this.educationCategoryName = educationCategory.getName();
        }
    }

    public Category getSalaryCategory() {
        return salaryCategory;
    }

    public void setSalaryCategory(Category salaryCategory) {
        this.salaryCategory = salaryCategory;
        if (salaryCategory != null) {
            this.salaryCategoryName = salaryCategory.getName();
        }
    }

    public String getSalaryCategoryName() {
        return salaryCategoryName;
    }

    public void setSalaryCategoryName(String salaryCategoryName) {
        this.salaryCategoryName = salaryCategoryName;
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
