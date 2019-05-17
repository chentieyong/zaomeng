package com.kingpivot.base.point.model;

import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "point")
public class Point extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键

    @Column(length = 36)
    private String memberID;//会员id

    @Column(length = 36)
    private String companyID;//公司id

    @Column(length = 36)
    private String shopID;//店铺id

    @Column(length = 36)
    private String applicationID;//应用ID

    @Column(length = 36)
    private String categoryID;//分类ID

    @Column(length = 36)
    private String siteID;//网站id

    @Column(length = 20)
    private Timestamp operateTime;//操作时间

    @Column(length = 100)
    private String action;//动作

    @Column(columnDefinition = "int default 0")
    private int actionType = 0;//方向 1获取，2消费

    @Column(columnDefinition = "int default 0")
    private int number = 0;//个数

    @Column(length = 36)
    private String objectDefineID;//操作对象类型

    @Column(length = 36)
    private String objectID;//操作对象ID

    @Column(length = 100)
    private String objectName; //操作对象名称

    @Column()
    private Date overDate;

    @Column(length = 36)
    private String pointDefineId;

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

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getSiteID() {
        return siteID;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }

    public Timestamp getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Timestamp operateTime) {
        this.operateTime = operateTime;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getObjectDefineID() {
        return objectDefineID;
    }

    public void setObjectDefineID(String objectDefineID) {
        this.objectDefineID = objectDefineID;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public Date getOverDate() {
        return overDate;
    }

    public void setOverDate(Date overDate) {
        this.overDate = overDate;
    }

    public String getPointDefineId() {
        return pointDefineId;
    }

    public void setPointDefineId(String pointDefineId) {
        this.pointDefineId = pointDefineId;
    }
}
