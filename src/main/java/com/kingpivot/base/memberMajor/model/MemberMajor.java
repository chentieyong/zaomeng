package com.kingpivot.base.memberMajor.model;

import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "memberMajor")
public class MemberMajor extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键
    @Column(length = 36)
    private String applicationID;
    @Column(length = 100)
    private String name;
    @Column(length = 20)
    private String phone;
    @Column(length = 500)
    private String description;
    @Column(length = 36)
    private String memberID;
    @Column(length = 36)
    private String majorID;
    @Column(length = 36)
    private String shengID;
    @Column(length = 36)
    private String shiID;
    @Column(length = 36)
    private String xianID;
    @Column(name = "status",columnDefinition = "int default 1")
    private int status;//1新申请,2通过,3拒绝

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getMajorID() {
        return majorID;
    }

    public void setMajorID(String majorID) {
        this.majorID = majorID;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShengID() {
        return shengID;
    }

    public void setShengID(String shengID) {
        this.shengID = shengID;
    }

    public String getShiID() {
        return shiID;
    }

    public void setShiID(String shiID) {
        this.shiID = shiID;
    }

    public String getXianID() {
        return xianID;
    }

    public void setXianID(String xianID) {
        this.xianID = xianID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}