package com.kingpivot.base.memberBonus.model;

import com.kingpivot.base.bonusDefine.model.BonusDefine;
import com.kingpivot.base.city.model.City;
import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "memberbonus")
public class MemberBonus extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键
    @Column(length = 100)
    private String name;

    @Column(length = 36)
    private String memberID;

    @Column(length = 36)
    private String applicationID;

    @Column(length = 36)
    private String bonusID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bonusID", insertable = false, updatable = false)  //不能保存和修改
    private BonusDefine bonusDefine;

    @Column(length = 36)
    private String memberOrderID;

    @Column
    private Double amount = 0.0;

    @Column
    private Timestamp startDate;

    @Column
    private Timestamp endDate;

    @Column
    private Timestamp useTime;

    @Column
    private Timestamp cancelTime;

    @Column
    private String printCode;

    @Column(name = "status",columnDefinition = "int default 0")
    private int status = 0; //0未开始，1可用，2已使用，3过期

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

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public String getBonusID() {
        return bonusID;
    }

    public void setBonusID(String bonusID) {
        this.bonusID = bonusID;
    }

    public String getMemberOrderID() {
        return memberOrderID;
    }

    public void setMemberOrderID(String memberOrderID) {
        this.memberOrderID = memberOrderID;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public Timestamp getUseTime() {
        return useTime;
    }

    public void setUseTime(Timestamp useTime) {
        this.useTime = useTime;
    }

    public Timestamp getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Timestamp cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getPrintCode() {
        return printCode;
    }

    public void setPrintCode(String printCode) {
        this.printCode = printCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public BonusDefine getBonusDefine() {
        return bonusDefine;
    }

    public void setBonusDefine(BonusDefine bonusDefine) {
        this.bonusDefine = bonusDefine;
    }
}
