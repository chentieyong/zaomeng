package com.kingpivot.base.objectFeatureData.model;

import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/6/2.
 */
@Entity
@Table(name = "objectFeatureData")
public class ObjectFeatureData extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;
    @Column(length = 100)
    private String name;//名称
    @Column(length = 2000)
    private String description;//描述
    @Column(length = 36)
    private String companyID; //公司ID
    @Column(length = 36)
    private String shopID; //店铺ID
    @Column(length = 36)
    private String objectID; //对象ID
    @Column(length = 36)
    private String objectFeatureItemID1; //对象特征选项ID1
    @Column(length = 36)
    private String objectFeatureItemID2; //对象特征选项ID2
    @Column(length = 36)
    private String objectFeatureItemID3; //对象特征选项ID3
    @Column(length = 36)
    private String objectFeatureItemID4; //对象特征选项ID4
    @Column(length = 36)
    private String objectFeatureItemID5; //对象特征选项ID5
    @Column
    private Double realPrice = 0.00d;//标准价格
    @Column()
    private Double standPrice = 0.00d;//标准价格
    @Column
    private Double memberPrice = 0.00d;//会员价格

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public String getObjectFeatureItemID1() {
        return objectFeatureItemID1;
    }

    public void setObjectFeatureItemID1(String objectFeatureItemID1) {
        this.objectFeatureItemID1 = objectFeatureItemID1;
    }

    public String getObjectFeatureItemID2() {
        return objectFeatureItemID2;
    }

    public void setObjectFeatureItemID2(String objectFeatureItemID2) {
        this.objectFeatureItemID2 = objectFeatureItemID2;
    }

    public String getObjectFeatureItemID3() {
        return objectFeatureItemID3;
    }

    public void setObjectFeatureItemID3(String objectFeatureItemID3) {
        this.objectFeatureItemID3 = objectFeatureItemID3;
    }

    public String getObjectFeatureItemID4() {
        return objectFeatureItemID4;
    }

    public void setObjectFeatureItemID4(String objectFeatureItemID4) {
        this.objectFeatureItemID4 = objectFeatureItemID4;
    }

    public String getObjectFeatureItemID5() {
        return objectFeatureItemID5;
    }

    public void setObjectFeatureItemID5(String objectFeatureItemID5) {
        this.objectFeatureItemID5 = objectFeatureItemID5;
    }

    public Double getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(Double realPrice) {
        this.realPrice = realPrice;
    }

    public Double getStandPrice() {
        return standPrice;
    }

    public void setStandPrice(Double standPrice) {
        this.standPrice = standPrice;
    }

    public Double getMemberPrice() {
        return memberPrice;
    }

    public void setMemberPrice(Double memberPrice) {
        this.memberPrice = memberPrice;
    }
}
