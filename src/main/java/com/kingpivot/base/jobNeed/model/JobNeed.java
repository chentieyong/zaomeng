package com.kingpivot.base.jobNeed.model;

import com.kingpivot.base.member.model.Member;
import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "jobNeed")
public class JobNeed extends BaseModel<String> {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;

    @Column()
    private String applicationID;//应用ID

    @Column(length = 36)
    private String memberID;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberID", insertable = false, updatable = false)  //不能保存和修改
    private Member member;

    @Column(length = 100)
    private String name;

    @Column(length = 100)
    private String shortName;

    @Column(length = 200)
    private String description;

    @Column()
    private Timestamp beginDate;

    @Column()
    private Timestamp endDate;

    @Column(name = "age", columnDefinition = "int default 0")
    private int age = 0;

    @Column(length = 36)
    private String titleID;//性别

    @Column(length = 36)
    private String salaryCategoryID;//薪资范围

    @Column(name = "workYears", columnDefinition = "int default 1")
    private int workYears = 1;//工作年限

    @Column(length = 36)
    private String educationCategoryID;//学历

    @Column(length = 100)
    private String address;//地址

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

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
