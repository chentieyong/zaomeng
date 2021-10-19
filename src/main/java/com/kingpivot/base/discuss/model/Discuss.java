package com.kingpivot.base.discuss.model;

import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.objectDefine.model.ObjectDefine;
import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "discuss")
public class Discuss extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键

    @Column(length = 36)
    private String applicationID;//应用id

    @Column(length = 100)
    private String name;//名称

    @Column(length = 200)
    private String description;

    @Column(length = 36)
    private String objectDefineID;//对象定义id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "objectDefineID", insertable = false, updatable = false)  //不能保存和修改
    private ObjectDefine objectDefine;

    @Column(length = 36)
    private String objectID;//对象id

    @Column(length = 100)
    private String objectName;//对象名

    @Column(length = 36)
    private String memberID;//评论人
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberID", insertable = false, updatable = false)  //不能保存和修改
    private Member member;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObjectDefineID() {
        return objectDefineID;
    }

    public void setObjectDefineID(String objectDefineID) {
        this.objectDefineID = objectDefineID;
    }

    public ObjectDefine getObjectDefine() {
        return objectDefine;
    }

    public void setObjectDefine(ObjectDefine objectDefine) {
        this.objectDefine = objectDefine;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
