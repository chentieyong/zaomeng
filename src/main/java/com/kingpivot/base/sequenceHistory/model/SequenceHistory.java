/*
<%--版权所有： 上海金枢信息科技有限公司，上海聚枢信息科技有限公司
        总设计：赵真利
        作者： 雷华健
        文件代码： 8af5993a4fa745bc014faadbcc3b038d。
        创建日期：2015-09-08 11:03
        文件说明：这是模块模块的动态网页，可实现增加修改删除锁定的页面操作。融资申请管理模块。  --%>*/
package com.kingpivot.base.sequenceHistory.model;

import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "sequenceHistory")
public class SequenceHistory extends BaseModel<String> {

    private static final long serialVersionUID = -237329947754114555L;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键

    @Column()
    private String sequenceDefineID;//序列号定义ID"
    @Column()
    private Integer currentValue;//当前数值"
    @Column()
    private String objectID;//对象ID"


    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSequenceDefineID() {
        return sequenceDefineID;
    }

    public void setSequenceDefineID(String sequenceDefineID) {
        this.sequenceDefineID = sequenceDefineID;
    }

    public Integer getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Integer currentValue) {
        this.currentValue = currentValue;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }
}
