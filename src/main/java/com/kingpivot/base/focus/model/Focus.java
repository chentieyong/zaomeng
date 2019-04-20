package com.kingpivot.base.focus.model;

import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by guanjun on 15-8-31.
 */
@Entity
@Table(name = "focus")
public class Focus extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键

    @Column(length = 36)
    private String companyID;//公司ID

    @Column(length = 36)
    private String siteID;//站点ID

    @Column(length = 100)
    private String Name;//名称
    @Column(length = 100)
    private String shortName;//简称

    @Column()
    private int actdirection;//滑动方向：1= 默认，从右边滑动到左边； 2= 左边到右边；3=上面到下面；4=下面到上面
    @Column()
    private int intervalTime;//滑动间隔：0= 系统定义的 ；1= 用户自己定义的

    @Column(length = 100)
    private String picturePath;//存储路径
    @Column(length = 200)
    private String description;//描述

    private Integer foucusType = 1;//轮播类型，默认首页轮播，2公司商城首页
    private int marginPX;//上下边距px

    @Column(length = 36)
    private String applicationID;

    @Column(length = 100)
    private String listImage;//列表图
    @Column(length = 100)
    private String faceImage;//压题图

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getSiteID() {
        return siteID;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getActdirection() {
        return actdirection;
    }

    public void setActdirection(int actdirection) {
        this.actdirection = actdirection;
    }

    public int getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(int intervalTime) {
        this.intervalTime = intervalTime;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getFoucusType() {
        return foucusType;
    }

    public void setFoucusType(Integer foucusType) {
        this.foucusType = foucusType;
    }

    public int getMarginPX() {
        return marginPX;
    }

    public void setMarginPX(int marginPX) {
        this.marginPX = marginPX;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
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
}


