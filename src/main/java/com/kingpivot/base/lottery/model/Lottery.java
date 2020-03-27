package com.kingpivot.base.lottery.model;

import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 抽奖定义
 */
@Entity
@Table(name = "lottery")
public class Lottery extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//职位需求ID

    @Column()
    private String applicationID;//应用ID

    @Column(length = 100)
    private String name;//名称

    @Column(length = 100)
    private String shortName;//简称

    @Column(name = "orderSeq", columnDefinition = "int default 1")
    private int orderSeq = 1;//排序号

    @Column(length = 500)
    private String description;//描述

    @Column(name = "doType", columnDefinition = "int default 1")
    private int doType = 1;//抽奖类型 1集中开奖 2立即开奖

    @Column(name = "usePoint", columnDefinition = "int default 1")
    private int usePoint = 1;//消耗积分

    @Column(length = 100)
    private String listImage;//列表图

    @Column(length = 100)
    private String faceImage;//压题图

    @Column()
    private Timestamp beginTime;//开始时间

    @Column()
    private Timestamp endTime;//结束时间

    @Column()
    private Timestamp openTime;//开奖时间

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

    public int getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(int orderSeq) {
        this.orderSeq = orderSeq;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDoType() {
        return doType;
    }

    public void setDoType(int doType) {
        this.doType = doType;
    }

    public int getUsePoint() {
        return usePoint;
    }

    public void setUsePoint(int usePoint) {
        this.usePoint = usePoint;
    }

    public String getListImage() {
        return listImage;
    }

    public void setListImage(String listImage) {
        this.listImage = listImage;
    }

    public String getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }

    public Timestamp getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Timestamp beginTime) {
        this.beginTime = beginTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Timestamp getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Timestamp openTime) {
        this.openTime = openTime;
    }
}

