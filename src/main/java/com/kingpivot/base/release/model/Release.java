package com.kingpivot.base.release.model;

import com.kingpivot.base.navigator.model.Navigator;
import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "`release`")
public class Release extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键  navigatorID

    @Column(length = 36)
    private String navigatorID;//导航ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "navigatorID", insertable = false, updatable = false)  //不能保存和修改
    private Navigator navigator;

    @Column(length = 36)
    private String objectID;

    @Column(length = 36)
    private String objectName;

    @Column(length = 36)
    private String objectDefineID;

    @Column(length = 100)
    private String articlepublishName;//名称

    @Column(length = 50)
    private String shortName;//简称

    @Column(length = 36)
    private String companyID;//公司ID
    @Column(length = 36)
    private String departmentID;//部门ID

    @Column
    private Timestamp startdate;//开始日期

    @Column
    private Timestamp endDate;//结束日期

    @Column(columnDefinition = "int default 0")
    private int isGood = 0;//是否精华

    @Column(columnDefinition = "int default 0")
    private int isTop = 0;//是否置顶

    @Column(columnDefinition = "int default 0")
    private int isDiscuss = 0;//是否可评论

    @Column(columnDefinition = "int default 1")
    private int orderSeq = 1;//排序

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNavigatorID() {
        return navigatorID;
    }

    public void setNavigatorID(String navigatorID) {
        this.navigatorID = navigatorID;
    }

    public Navigator getNavigator() {
        return navigator;
    }

    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
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

    public String getObjectDefineID() {
        return objectDefineID;
    }

    public void setObjectDefineID(String objectDefineID) {
        this.objectDefineID = objectDefineID;
    }

    public String getArticlepublishName() {
        return articlepublishName;
    }

    public void setArticlepublishName(String articlepublishName) {
        this.articlepublishName = articlepublishName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(String departmentID) {
        this.departmentID = departmentID;
    }

    public Timestamp getStartdate() {
        return startdate;
    }

    public void setStartdate(Timestamp startdate) {
        this.startdate = startdate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public int getIsGood() {
        return isGood;
    }

    public void setIsGood(int isGood) {
        this.isGood = isGood;
    }

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public int getIsDiscuss() {
        return isDiscuss;
    }

    public void setIsDiscuss(int isDiscuss) {
        this.isDiscuss = isDiscuss;
    }

    public int getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(int orderSeq) {
        this.orderSeq = orderSeq;
    }
}
