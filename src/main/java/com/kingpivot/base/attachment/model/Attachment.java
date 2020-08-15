package com.kingpivot.base.attachment.model;

import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "attachment")
public class Attachment extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//附件ID"

    @Column(length = 36)
    private String objectID;//对象ID"

    @Column(length = 36)
    private String ObjectDefineID;//"

    @Column(length = 100)
    private String name;

    @Column(length = 100)
    private String url;//文件URL"

    @Column()
    private int orderSeq;//序号"

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(int orderSeq) {
        this.orderSeq = orderSeq;
    }

    public String getObjectDefineID() {
        return ObjectDefineID;
    }

    public void setObjectDefineID(String objectDefineID) {
        ObjectDefineID = objectDefineID;
    }
}