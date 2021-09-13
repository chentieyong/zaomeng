package com.kingpivot.base.blessingTreeDetail.model;

import com.kingpivot.base.blessingTree.model.BlessingTree;
import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 祈福树明细
 */
@Entity
@Table(name = "blessingTreeDetail")
public class BlessingTreeDetail extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//祈福树明细ID

    @Column(length = 30)
    private String name;

    @Column(length = 36)
    private String applicationID;//应用ID

    @Column(length = 200)
    private String description;//描述

    @Column(length = 100)
    private String faceImage;//押题图

    @Column(name = "number", columnDefinition = "int default 1")
    private int number = 1;//个数

    @Column(length = 36)
    private String blessingTreeID;//祈福树ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blessingTreeID", insertable = false, updatable = false)  //不能保存和修改
    private BlessingTree blessingTree;

    @Column(name = "awardType", columnDefinition = "int default 0")
    private int awardType = 0;//奖项类型，0：谢谢参与，1：积分

    @Column(name = "awardNumber", columnDefinition = "int default 1")
    private int awardNumber = 1;//奖项个数

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

    public String getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getBlessingTreeID() {
        return blessingTreeID;
    }

    public void setBlessingTreeID(String blessingTreeID) {
        this.blessingTreeID = blessingTreeID;
    }

    public BlessingTree getBlessingTree() {
        return blessingTree;
    }

    public void setBlessingTree(BlessingTree blessingTree) {
        this.blessingTree = blessingTree;
    }

    public int getAwardType() {
        return awardType;
    }

    public void setAwardType(int awardType) {
        this.awardType = awardType;
    }

    public int getAwardNumber() {
        return awardNumber;
    }

    public void setAwardNumber(int awardNumber) {
        this.awardNumber = awardNumber;
    }
}

