package com.kingpivot.base.memberRecharge.model;

import com.kingpivot.base.member.model.Member;
import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "memberRecharge")
public class MemberRecharge extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键
    @Column(length = 100)
    private String name;//名称
    @Column(length = 36)
    private String memberID;//会员id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberID", insertable = false, updatable = false)  //不能保存和修改
    private Member member;
    @Column(length = 36)
    private String companyID;//公司
    @Column(name = "payTotal", columnDefinition = "double default 0")
    private int payTotal = 0; //支付金额
    @Column(name = "amount", columnDefinition = "double default 0")
    private int amount = 0; //实际金额
    @Column()
    private Timestamp payTime;//支付时间
    @Column(length = 36)
    private String paywayID;//支付机构ID
    @Column(length = 100)
    private String paySequence;//付款流水号
    @Column(name = "status", columnDefinition = "double default 1")
    private int status = 1; //1新，2已支付

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

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public int getPayTotal() {
        return payTotal;
    }

    public void setPayTotal(int payTotal) {
        this.payTotal = payTotal;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Timestamp getPayTime() {
        return payTime;
    }

    public void setPayTime(Timestamp payTime) {
        this.payTime = payTime;
    }

    public String getPaywayID() {
        return paywayID;
    }

    public void setPaywayID(String paywayID) {
        this.paywayID = paywayID;
    }

    public String getPaySequence() {
        return paySequence;
    }

    public void setPaySequence(String paySequence) {
        this.paySequence = paySequence;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
