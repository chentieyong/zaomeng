package com.kingpivot.base.buyNeed.model;

import com.kingpivot.base.member.model.Member;
import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;


@Entity
@Table(name = "buyNeed")
public class BuyNeed extends BaseModel<String> {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",strategy="uuid")
    @Column(length=36)
    private String id;
    @Column(length = 36)
    private String applicationID;

    @Column(length = 36)
    private String memberID;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberID", insertable = false, updatable = false)  //不能保存和修改
    private Member member;

    @Column(length = 100)
    private String name;

    @Column(length = 100)
    private String shortName;

    @Column(length = 2000)
    private String description;

    @Column
    private Timestamp beginDate;

    @Column
    private Timestamp endDate;

    @Column(name = "beginAmount", columnDefinition = "int default 1")
    private int beginAmount;

    @Column(name = "endAmount", columnDefinition = "int default 1")
    private int endAmount;

    @Column(length = 100)
    private String fromWhere;//产地

    @Column(name = "qty", columnDefinition = "int default 1")
    private int qty = 1;//数量

    @Column(length = 100)
    private String priceUnit;//价格单位

    @Column(name = "status", columnDefinition = "int default 1")
    private int status = 1;//1新

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

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Timestamp beginDate) {
        this.beginDate = beginDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public int getBeginAmount() {
        return beginAmount;
    }

    public void setBeginAmount(int beginAmount) {
        this.beginAmount = beginAmount;
    }

    public int getEndAmount() {
        return endAmount;
    }

    public void setEndAmount(int endAmount) {
        this.endAmount = endAmount;
    }

    public String getFromWhere() {
        return fromWhere;
    }

    public void setFromWhere(String fromWhere) {
        this.fromWhere = fromWhere;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        this.priceUnit = priceUnit;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
