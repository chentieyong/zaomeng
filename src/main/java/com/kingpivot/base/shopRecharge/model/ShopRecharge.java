package com.kingpivot.base.shopRecharge.model;

import com.kingpivot.base.shop.model.Shop;
import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "shopRecharge")
public class ShopRecharge extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键

    @Column(length = 100)
    private String name;

    @Column(length = 36)
    private String applicationID;

    @Column(length = 36)
    private String companyID;

    @Column(length = 36)
    private String shopRechargeDefineID;

    @Column(length = 36)
    private String shopID;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopID", insertable = false, updatable = false)  //不能保存和修改
    private Shop shop;

    @Column()
    private Double rechargeAmount;//充值金额

    @Column()
    private Double amount;//实际金额

    @Column()
    private Timestamp rechargeTime;//充值时间

    @Column()
    private String auditDescription;//审核说明

    @Column()
    private String transactionId;//付款流水号

    @Column()
    private Integer status = 1;//状态 1申请中 2已确认 3已驳回

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

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getShopRechargeDefineID() {
        return shopRechargeDefineID;
    }

    public void setShopRechargeDefineID(String shopRechargeDefineID) {
        this.shopRechargeDefineID = shopRechargeDefineID;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public Double getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(Double rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Timestamp getRechargeTime() {
        return rechargeTime;
    }

    public void setRechargeTime(Timestamp rechargeTime) {
        this.rechargeTime = rechargeTime;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public String getAuditDescription() {
        return auditDescription;
    }

    public void setAuditDescription(String auditDescription) {
        this.auditDescription = auditDescription;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
