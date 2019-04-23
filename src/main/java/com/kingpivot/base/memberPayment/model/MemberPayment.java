package com.kingpivot.base.memberPayment.model;

import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "memberPayment")
public class MemberPayment extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//会员支付ID
    @Column(length = 100)
    private String name;//名称
    @Column(length = 200)
    private String description;//备注
    @Column(length = 50)
    private String orderCode;//订单编号
    @Column(length = 36)
    private String applicationID;
    @Column(length = 36)
    private String objectID;//对象ID
    @Column(length = 100)
    private String objectName;//对象名称
    @Column(length = 36)
    private String memberID;//会员ID
    @Column()
    private Double amount;//金额
    @Column()
    private Timestamp applyTime;//申请时间
    @Column()
    private Timestamp cancelTime;//取消时间
    @Column()
    private Integer payFrom = 1;//付款方式，默认第三方支付
    @Column()
    private Timestamp payTime;//付款时间
    @Column(length = 100)
    private String paySequence;//付款流水号
    @Column(length = 36)
    private String paywayID;
    @Column(length = 36)
    private String objectDefineID;
    @Column()
    private Integer status;//状态1=新请求2=撤销3=付款完成

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Timestamp getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Timestamp applyTime) {
        this.applyTime = applyTime;
    }

    public Timestamp getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Timestamp cancelTime) {
        this.cancelTime = cancelTime;
    }

    public Integer getPayFrom() {
        return payFrom;
    }

    public void setPayFrom(Integer payFrom) {
        this.payFrom = payFrom;
    }

    public Timestamp getPayTime() {
        return payTime;
    }

    public void setPayTime(Timestamp payTime) {
        this.payTime = payTime;
    }

    public String getPaySequence() {
        return paySequence;
    }

    public void setPaySequence(String paySequence) {
        this.paySequence = paySequence;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPaywayID() {
        return paywayID;
    }

    public void setPaywayID(String paywayID) {
        this.paywayID = paywayID;
    }

    public String getObjectDefineID() {
        return objectDefineID;
    }

    public void setObjectDefineID(String objectDefineID) {
        this.objectDefineID = objectDefineID;
    }
}