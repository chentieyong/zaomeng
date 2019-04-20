package com.kingpivot.base.objectDefine.model;

import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "objectdefine")
public class ObjectDefine extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键

    @Column(length = 100)
    private String name;//名称

    @Column()
    private String shortName;//简称

    @Column()
    private Integer isWorkflow;//是否工作流 1.是 0.否

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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Integer getIsWorkflow() {
        return isWorkflow;
    }

    public void setIsWorkflow(Integer isWorkflow) {
        this.isWorkflow = isWorkflow;
    }
}
