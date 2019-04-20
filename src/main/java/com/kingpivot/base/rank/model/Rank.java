package com.kingpivot.base.rank.model;

import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "rank")
public class Rank extends BaseModel<String> {
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
    private String shortname;
    @Column(length = 11)
    private Integer orderSeq = 1;
    @Column(length = 100)
    private String listImage;
    @Column(length = 100)
    private String faceImage;
    @Column()
    private Double upPoint = 0.0;
    @Column()
    private Double depositeRate = 0.0;
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

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
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

    public Double getUpPoint() {
        return upPoint;
    }

    public void setUpPoint(Double upPoint) {
        this.upPoint = upPoint;
    }

    public Double getDepositeRate() {
        return depositeRate;
    }

    public void setDepositeRate(Double depositeRate) {
        this.depositeRate = depositeRate;
    }
}
