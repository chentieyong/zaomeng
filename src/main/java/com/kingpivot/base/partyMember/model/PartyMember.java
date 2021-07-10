package com.kingpivot.base.partyMember.model;

import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "partyMember")
public class PartyMember extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键

    @Column(length = 100)
    private String name;//党员姓名

    @Column(length = 36)
    private String applicationID;//应用ID

    @Column(length = 20)
    private String joinYear;//加入年份

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

    public String getJoinYear() {
        return joinYear;
    }

    public void setJoinYear(String joinYear) {
        this.joinYear = joinYear;
    }
}
