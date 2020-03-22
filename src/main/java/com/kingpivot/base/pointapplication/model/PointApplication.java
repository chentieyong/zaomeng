package com.kingpivot.base.pointapplication.model;

import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "pointApplication")
public class PointApplication extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键

    @Column(length = 100)
    private String name;

    @Column(length = 100)
    private Integer number;//积分个数

    @Column()
    private String applicationID;//应用ID

    @Column()
    private String pointDefineID;//积分定义ID

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

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public String getPointDefineID() {
        return pointDefineID;
    }

    public void setPointDefineID(String pointDefineID) {
        this.pointDefineID = pointDefineID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
