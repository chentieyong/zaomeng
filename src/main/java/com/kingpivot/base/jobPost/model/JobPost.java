package com.kingpivot.base.jobPost.model;

import com.kingpivot.base.member.model.Member;
import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "jobPost")
public class JobPost extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//职位需求ID

    @Column()
    private String applicationID;//应用ID

    @Column(length = 100)
    private String companyName;//公司名称

    @Column(length = 100)
    private String companyAddress;//公司地址

    @Column(length = 100)
    private String phone;//电话

    @Column(length = 36)
    private String memberID;//会员ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberID", insertable = false, updatable = false)  //不能保存和修改
    private Member member;

    @Column(length = 100)
    private String name;//名称

    @Column(length = 100)
    private String shortName;//简称

    @Column(length = 200)
    private String description;//描述

    @Column()
    private Timestamp beginDate;//开始日期

    @Column()
    private Timestamp endDate;//结束日期

    @Column(name = "needNumber", columnDefinition = "int default 1")
    private int needNumber = 1;//需求个数

    @Column(length = 36)
    private String educationCategoryID;//学历要求

    @Column
    private String salaryCategoryID;//资金待遇

    @Column(name = "status", columnDefinition = "int default 1")
    private int status = 1;//状态 1新

    @Override
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

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}

