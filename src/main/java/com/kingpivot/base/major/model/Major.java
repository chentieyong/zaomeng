package com.kingpivot.base.major.model;

import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "major")
public class Major extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键
    @Column(length = 36)
    private String companyID;//公司ID

    @Column(length = 36)
    private String applicationID;

    @Column(length = 100)
    private String name;

    @Column(length = 20)
    private String shortName;

    @Column(length = 11)
    private Integer orderSeq = 1;

    @Column(length = 100)
    private String listImage;

    @Column(length = 100)
    private String faceImage;

    @Column(name = "applyType", columnDefinition = "int default 1")
    private int applyType;//1注册赠送，2会员申请，3后台赠送

    @Column(name = "upgradeNumber", columnDefinition = "int default 1")
    private int upgradeNumber = 1;//升级个数

    @Column(name = "alreadyUpgradeNumber", columnDefinition = "int default 0")
    private int alreadyUpgradeNumber = 0;//已经升级个数

    @Column(name = "maxFollows", columnDefinition = "int default 1")
    private int maxFollows = 1;//最多粉丝数量

    @Column(name = "price", columnDefinition = "double default 1")
    private double price = 0d;//申请价格

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

    public Integer getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(Integer orderSeq) {
        this.orderSeq = orderSeq;
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

    public int getApplyType() {
        return applyType;
    }

    public void setApplyType(int applyType) {
        this.applyType = applyType;
    }

    public int getUpgradeNumber() {
        return upgradeNumber;
    }

    public void setUpgradeNumber(int upgradeNumber) {
        this.upgradeNumber = upgradeNumber;
    }

    public int getAlreadyUpgradeNumber() {
        return alreadyUpgradeNumber;
    }

    public void setAlreadyUpgradeNumber(int alreadyUpgradeNumber) {
        this.alreadyUpgradeNumber = alreadyUpgradeNumber;
    }

    public int getMaxFollows() {
        return maxFollows;
    }

    public void setMaxFollows(int maxFollows) {
        this.maxFollows = maxFollows;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
