package com.kingpivot.base.memberstatistics.model;

import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "memberstatistics")
public class MemberStatistics extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键

    @Column(length = 100)
    private String name;//名称

    @Column(length = 36)
    private String memberID;//会员ID

    @Column(length = 36)
    private String applicationID;//应用ID

    @Column()
    private Integer point = 0;//现实积分

    @Column()
    private Integer pointTotalUsed = 0;//累计消耗积分

    @Column()
    private Integer pointTotal = 0;//累计积分

    @Column()
    private Double cashBalance = 0d;//现金余额"

    @Column()
    private Double cashTotalRecharge = 0d;//累计充值"

    @Column(name = "cashTotalIncome", columnDefinition = "double default 0")
    private double cashTotalIncome = 0d;//累计收入

    @Column(name = "monthBalance", columnDefinition = "double default 0")
    private double monthBalance = 0d;//月结额度


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

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Integer getPointTotal() {
        return pointTotal;
    }

    public void setPointTotal(Integer pointTotal) {
        this.pointTotal = pointTotal;
    }

    public Double getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(Double cashBalance) {
        this.cashBalance = cashBalance;
    }

    public Double getCashTotalRecharge() {
        return cashTotalRecharge;
    }

    public void setCashTotalRecharge(Double cashTotalRecharge) {
        this.cashTotalRecharge = cashTotalRecharge;
    }

    public Integer getPointTotalUsed() {
        return pointTotalUsed;
    }

    public void setPointTotalUsed(Integer pointTotalUsed) {
        this.pointTotalUsed = pointTotalUsed;
    }

    public double getCashTotalIncome() {
        return cashTotalIncome;
    }

    public void setCashTotalIncome(double cashTotalIncome) {
        this.cashTotalIncome = cashTotalIncome;
    }

    public double getMonthBalance() {
        return monthBalance;
    }

    public void setMonthBalance(double monthBalance) {
        this.monthBalance = monthBalance;
    }
}
