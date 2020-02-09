package com.kingpivot.base.friendNeed.model;

import com.kingpivot.base.category.model.Category;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "friendNeed")
public class FriendNeed extends BaseModel<String> {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;

    @Column(length = 36)
    private String applicationID;

    @Column(length = 36)
    private String memberID;//发布人
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberID", insertable = false, updatable = false)  //不能保存和修改
    private Member member;

    @Column(length = 100)
    private String name;

    @Column(length = 100)
    private String shortName;

    @Column(length = 200)
    private String description;

    @Column
    private Timestamp beginDate;//开始日期

    @Column
    private Timestamp endDate;//结束日期

    @Column(name = "age", columnDefinition = "int default 0")
    private int age;//年龄

    @Column(length = 36)
    private String titleID;//性别

    @Column(length = 36)
    private String salaryCategoryID;//收入范围

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salaryCategoryID", insertable = false, updatable = false)  //不能保存和修改
    private Category salaryCategory;

    @Column(length = 100)
    private String phone;//电话

    @Column(length = 100)
    private String email;//邮箱

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public Category getSalaryCategory() {
        return salaryCategory;
    }

    public void setSalaryCategory(Category salaryCategory) {
        this.salaryCategory = salaryCategory;
    }
}
