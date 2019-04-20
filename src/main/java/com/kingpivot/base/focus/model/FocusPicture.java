package com.kingpivot.base.focus.model;

import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by guanjun on 15-8-31.
 */
@Entity
@Table(name = "focuspicture")
public class FocusPicture extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键

    @Column(length = 36)
    private String focusID;

    @Column(columnDefinition = "int default 1")
    private Integer orderSeq;

    @Column(length = 100)
    private String picturePath;//图片路径

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFocusID() {
        return focusID;
    }

    public void setFocusID(String focusID) {
        this.focusID = focusID;
    }

    public Integer getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(Integer orderSeq) {
        this.orderSeq = orderSeq;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }
}