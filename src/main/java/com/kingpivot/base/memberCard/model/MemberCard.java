package com.kingpivot.base.memberCard.model;

import com.kingpivot.base.cardDefine.model.CardDefine;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;


@Entity
@Table(name = "memberCard")
public class MemberCard extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键
    @Column(length = 36)
    private String applicationID;
    @Column(length = 100)
    private String name;
    @Column(length = 36)
    private String memberID;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberID", insertable = false, updatable = false)  //不能保存和修改
    private Member member;
    @Column(length = 36)
    private String cardDefineID;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cardDefineID", insertable = false, updatable = false)  //不能保存和修改
    private CardDefine cardDefine;
    @Column
    private Timestamp beginTime;
    @Column
    private Timestamp endTime;

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

    public String getCardDefineID() {
        return cardDefineID;
    }

    public void setCardDefineID(String cardDefineID) {
        this.cardDefineID = cardDefineID;
    }

    public CardDefine getCardDefine() {
        return cardDefine;
    }

    public void setCardDefine(CardDefine cardDefine) {
        this.cardDefine = cardDefine;
    }

    public Timestamp getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Timestamp beginTime) {
        this.beginTime = beginTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }
}