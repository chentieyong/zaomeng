package com.kingpivot.base.category.model;

import com.kingpivot.common.model.BaseModel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
public class Category extends BaseModel<String> {
    @Id
    @Column(length = 36)
    private String id;//分类定义ID"

    @Column(length = 36)
    private String parentID;//父项ID"

    @Column()
    private String applicationID;//应用ID

    @Column()
    private String companyID;//公司ID

    @Column()
    private String shopID;//店铺ID

    @Column()
    private String objectDefineID;//对象定义ID

    @Column(length = 100)
    private String name;//名称"

    @Column(length = 20)
    private String shortName;//简称"

    @Column()
    private String description;//描述

    @Column()
    private String largeIcon;//大图标

    @Column()
    private String smallIcon;//小图标

    @Column(length = 36)
    private String code;//分类表的键

    @Column(columnDefinition = "bigint default 0")
    private long orderSeq = 0;//排序号

    @Column()
    private String parameter1;//参数1

    @Column()
    private String parameter2;//参数2

    @Column()
    private String parameter3;//参数3

    @Column(columnDefinition = "int default 0")
    private int depth;      // 位置

    @Column(name = "isLeaf")
    private boolean isLeaf;  //是否是叶节点

    @Column(columnDefinition = "int default 0")
    private int leftWeight = 0;

    @Column(columnDefinition = "int default 0")
    private int rightWeight = 0;

    @Transient
    private List nodes = new ArrayList();

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
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

    public String getObjectDefineID() {
        return objectDefineID;
    }

    public void setObjectDefineID(String objectDefineID) {
        this.objectDefineID = objectDefineID;
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

    public String getLargeIcon() {
        return largeIcon;
    }

    public void setLargeIcon(String largeIcon) {
        this.largeIcon = largeIcon;
    }

    public String getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(String smallIcon) {
        this.smallIcon = smallIcon;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(long orderSeq) {
        this.orderSeq = orderSeq;
    }

    public String getParameter1() {
        return parameter1;
    }

    public void setParameter1(String parameter1) {
        this.parameter1 = parameter1;
    }

    public String getParameter2() {
        return parameter2;
    }

    public void setParameter2(String parameter2) {
        this.parameter2 = parameter2;
    }

    public String getParameter3() {
        return parameter3;
    }

    public void setParameter3(String parameter3) {
        this.parameter3 = parameter3;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public int getLeftWeight() {
        return leftWeight;
    }

    public void setLeftWeight(int leftWeight) {
        this.leftWeight = leftWeight;
    }

    public int getRightWeight() {
        return rightWeight;
    }

    public void setRightWeight(int rightWeight) {
        this.rightWeight = rightWeight;
    }

    public List getNodes() {
        return nodes;
    }

    public void setNodes(List nodes) {
        this.nodes = nodes;
    }
}
