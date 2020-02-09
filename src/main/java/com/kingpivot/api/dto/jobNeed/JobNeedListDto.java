package com.kingpivot.api.dto.jobNeed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kingpivot.base.category.model.Category;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.common.utils.TimeTest;

import java.sql.Timestamp;

public class JobNeedListDto {
    private String id;

    private String applicationID;//应用ID

    private String memberID;

    @JsonIgnore
    private Member member;

    private String memberName;

    private String avatarURL;//头像URL

    private String memberCompanyName;

    private String memberJobName;

    private String name;

    private String shortName;

    private String description;

    private Timestamp beginDate;

    private String beginDateStr;

    private Timestamp endDate;

    private String endDateStr;

    private int age = 0;

    private String titleID;//性别

    private String salaryCategoryID;//薪资范围

    private String salaryCategoryName;//资金待遇

    @JsonIgnore
    private Category salaryCategory;

    private int workYears = 1;//工作年限

    private String educationCategoryID;//学历

    private String educationCategoryName;//学历要求

    @JsonIgnore
    private Category educationCategory;

    private String address;//地址

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getTitleID() {
        return titleID;
    }

    public void setTitleID(String titleID) {
        this.titleID = titleID;
    }

    public String getSalaryCategoryID() {
        return salaryCategoryID;
    }

    public void setSalaryCategoryID(String salaryCategoryID) {
        this.salaryCategoryID = salaryCategoryID;
    }

    public int getWorkYears() {
        return workYears;
    }

    public void setWorkYears(int workYears) {
        this.workYears = workYears;
    }

    public String getEducationCategoryID() {
        return educationCategoryID;
    }

    public void setEducationCategoryID(String educationCategoryID) {
        this.educationCategoryID = educationCategoryID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getSalaryCategoryName() {
        return salaryCategoryName;
    }

    public void setSalaryCategoryName(String salaryCategoryName) {
        this.salaryCategoryName = salaryCategoryName;
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
