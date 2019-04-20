package com.kingpivot.common.domain;

import com.kingpivot.common.model.BaseModel;
import com.kingpivot.common.util.Constants;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * 需要标记删除的实体继承该类
 *
 * @author chentieyong
 * @version v1.0
 * @time 2017年8月9日 下午1:52:35
 */
@MappedSuperclass
public abstract class MarkDeleteableModel<ID extends Serializable> extends BaseModel<ID> {

    private static final long serialVersionUID = -1880548221110317053L;

    @ApiModelProperty(value = "标记删除字段 1未删除 0已删除 ")
    @Column(name = "del", columnDefinition = "tinyint default 0")
    private int del = Constants.ISVALID_YES;

    public int getDel() {
        return del;
    }

    public void setDel(int del) {
        this.del = del;
    }

    @Override
    public String toString() {
        return "MarkDeleteableModel [del=" + del + "]";
    }

}
