package com.kingpivot.base.bonusDefine.model;

import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "bonusdefine")
public class BonusDefine extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键
    @Column(length = 100)
    private String name;//名称
    @Column(name = "getType", columnDefinition = "int default 1")
    private int getType = 1; //发放类型，1系统自动，2手动领取
    @Column(length = 36)
    private String companyID;//公司
    @Column(length = 100)
    private String listImage;
    @Column(length = 100)
    private String faceImage;
    @Column(length = 11)
    private double price;
    @Column(name = "maxNumber", columnDefinition = "int default 0")
    private int maxNumber = 0;
    @Column()
    private Timestamp startDate;//开始日期
    @Column()
    private Timestamp endDate;//结束日期

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

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getGetType() {
        return getType;
    }

    public void setGetType(int getType) {
        this.getType = getType;
    }

    public int getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }
}
