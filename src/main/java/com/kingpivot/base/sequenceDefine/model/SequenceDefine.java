package com.kingpivot.base.sequenceDefine.model;

import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "sequenceDefine")
public class SequenceDefine extends BaseModel<String> {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键

    @Column(length = 100)
    private String name;//名称"

    @Column(length = 100)
    private String code;//编码"

    @Column(length = 200)
    private String description;//说明"

    @Column(length = 100)
    private String formatString;//格式定义"

    @Column()
    private Integer timeScope;//时间范围"

    @Column()
    private Integer initValue = 1;//初始值"

    @Column()
    private Integer nextValue = this.initValue;

    @Column
    private Boolean isInit = true;

    @Column
    private Integer initTimeInt;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFormatString() {
        return formatString;
    }

    public void setFormatString(String formatString) {
        this.formatString = formatString;
    }

    public Integer getTimeScope() {
        return timeScope;
    }

    public void setTimeScope(Integer timeScope) {
        this.timeScope = timeScope;
    }

    public Integer getInitValue() {
        return initValue;
    }

    public void setInitValue(Integer initValue) {
        this.initValue = initValue;
    }

    public Integer getNextValue() {
        return nextValue;
    }

    public void setNextValue(Integer nextValue) {
        this.nextValue = nextValue;
    }

    public Boolean getIsInit() {
        return isInit;
    }

    public void setIsInit(Boolean isInit) {
        this.isInit = isInit;
    }

    public Integer getInitTimeInt() {
        return initTimeInt;
    }

    public void setInitTimeInt(Integer initTimeInt) {
        this.initTimeInt = initTimeInt;
    }
}
