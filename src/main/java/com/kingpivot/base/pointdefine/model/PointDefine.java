package com.kingpivot.base.pointdefine.model;

import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "pointDefine")
public class PointDefine extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键
    @Column(length = 100)
    private String name;
    @Column()
    private Integer orderSeq;
    @Column()
    private Integer overType;
    @Column()
    private String listImage;
    @Column(length = 100)
    private Integer number;//积分个数
    @Column()
    private int actionType;
    @Column()
    private int pointType;
    @Column(length = 20)
    private String shortName;
    @Column(length = 200)
    private String description;

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

    public Integer getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(Integer orderSeq) {
        this.orderSeq = orderSeq;
    }

    public Integer getOverType() {
        return overType;
    }

    public void setOverType(Integer overType) {
        this.overType = overType;
    }

    public String getListImage() {
        return listImage;
    }

    public void setListImage(String listImage) {
        this.listImage = listImage;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public int getPointType() {
        return pointType;
    }

    public void setPointType(int pointType) {
        this.pointType = pointType;
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
}
