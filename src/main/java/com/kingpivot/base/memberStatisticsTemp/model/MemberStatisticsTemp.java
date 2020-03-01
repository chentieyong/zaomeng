package com.kingpivot.base.memberStatisticsTemp.model;

import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "memberStatistics_temp")
public class MemberStatisticsTemp extends BaseModel<String> {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",strategy="uuid")
    @Column(length=36)
    private String id;//主键

    @Column(length=36)
    private String memberID;//会员ID

    private int point = 0;//现实积分

    private Double cashBalance = 0d;//现金余额"

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public Double getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(Double cashBalance) {
        this.cashBalance = cashBalance;
    }

    @Override
    public String toString() {
        return "MemberStatisticsTemp{" +
                "id='" + id + '\'' +
                ", memberID='" + memberID + '\'' +
                ", point=" + point +
                ", cashBalance=" + cashBalance +
                '}';
    }
}
