package com.kingpivot.base.memberBalance.model;

import com.kingpivot.base.member.model.Member;
import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "memberBalance")
public class MemberBalance extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键
    @Column(length = 36)
    private String siteID;//站点id
    @Column(length = 36)
    private String applicationID;//应用id
    @Column(length = 100)
    private String name; //名称
    @Column(length = 36)
    private String memberID; //会员
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberID", insertable = false, updatable = false)  //不能保存和修改
    private Member member;
    @Column(name = "orderSeq", columnDefinition = "int default 1")
    private Integer orderSeq = 1; //序号
    @Column(length = 20)
    private int operateType; //操作类型-1充值，2购买商品
    @Column(length = 36)
    private String objectDefineID; //操作对象类型
    @Column(length = 36)
    private String objectID; //操作对象ID
    @Column(length = 100)
    private String objectName; //操作对象名称
    @Column(name = "amount", columnDefinition = "int default 0")
    private double amount = 0; //操作金额
    @Column(name = "balance", columnDefinition = "int default 0")
    private Double balance; //当前余额
    @Column(length = 200)
    private String description; //描述
    @Column(length = 200)
    private String verification; //校验码
    @Column()
    private Timestamp operateTime;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSiteID() {
        return siteID;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
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

    public Integer getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(Integer orderSeq) {
        this.orderSeq = orderSeq;
    }

    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }

    public String getObjectDefineID() {
        return objectDefineID;
    }

    public void setObjectDefineID(String objectDefineID) {
        this.objectDefineID = objectDefineID;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    public Timestamp getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Timestamp operateTime) {
        this.operateTime = operateTime;
    }
}
