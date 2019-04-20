package com.kingpivot.base.objectFeatureItem.model;

import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/6/2.
 */
@Entity
@Table(name = "objectFeatureItem")
public class ObjectFeatureItem extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;

    @Column(length = 36)
    private String featureDefineID;//特征定义ID

    @Column(length = 100)
    private String featureDefineName;//特征定义名称

    @Column()
    private Integer featireDefineOrderSeq;//特征定义序号

    @Column(length = 36)
    private String objectID;//对象ID

    @Column(length = 100)
    private String name;//名称

    @Column(length = 100)
    private String shortName;//简称

    @Column()
    private Integer orderSeq;//序号

    @Column(length = 200)
    private String listImage;//列表图

    @Column(length = 200)
    private String faceImage;//押题图

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFeatureDefineID() {
        return featureDefineID;
    }

    public void setFeatureDefineID(String featureDefineID) {
        this.featureDefineID = featureDefineID;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
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

    public String getFeatureDefineName() {
        return featureDefineName;
    }

    public void setFeatureDefineName(String featureDefineName) {
        this.featureDefineName = featureDefineName;
    }

    public Integer getFeatireDefineOrderSeq() {
        return featireDefineOrderSeq;
    }

    public void setFeatireDefineOrderSeq(Integer featireDefineOrderSeq) {
        this.featireDefineOrderSeq = featireDefineOrderSeq;
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
