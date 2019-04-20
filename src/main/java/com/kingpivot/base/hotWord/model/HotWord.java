package com.kingpivot.base.hotWord.model;

import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "hotWord")
public class HotWord extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//热词ID

    @Column(length = 36)
    private String applicationID;//应用ID

    @Column(length = 36)
    private String siteID;//站点ID

    @Column(length = 36)
    private String companyID;//公司ID

    @Column(length = 36)
    private String searchDefineID;//搜索定义ID

    @Column(length = 100)
    private String name;//名称

    private Integer orderSeq;//排序

    @Column(length = 36)
    private String objectDefineID;//对象类型

    @Column(length = 36)
    private String objectID;//对象

    @Column(length = 100)
    private String objectName;//对象名称

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

    public String getSiteID() {
        return siteID;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getSearchDefineID() {
        return searchDefineID;
    }

    public void setSearchDefineID(String searchDefineID) {
        this.searchDefineID = searchDefineID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(Integer orderSeq) {
        this.orderSeq = orderSeq;
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
}
