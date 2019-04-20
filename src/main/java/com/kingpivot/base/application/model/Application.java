package com.kingpivot.base.application.model;

import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "application")
public class Application extends BaseModel<String> implements Serializable {
    private static final long serialVersionUID = -1300306668752973062L;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键

    @Column(length = 100)
    private String name;//名称

    @Column(length=36)
    private String smsWayID;//短信通道ID

    @Column()
    private Integer isTesting;//是否可测试,1 是 0 否

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

    public String getSmsWayID() {
        return smsWayID;
    }

    public void setSmsWayID(String smsWayID) {
        this.smsWayID = smsWayID;
    }

    public Integer getIsTesting() {
        return isTesting;
    }

    public void setIsTesting(Integer isTesting) {
        this.isTesting = isTesting;
    }
}
