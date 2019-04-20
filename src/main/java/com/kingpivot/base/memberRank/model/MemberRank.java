package com.kingpivot.base.memberRank.model;

import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.rank.model.Rank;
import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;


@Entity
@Table(name = "memberrank")
public class MemberRank extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键
    @Column(length = 100)
    private String name;
    @Column(length = 36)
    private String memberID;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberID", insertable = false, updatable = false)  //不能保存和修改
    private Member member;
    @Column(length = 36)
    private String rankID;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rankID", insertable = false, updatable = false)  //不能保存和修改
    private Rank rank;
    @Column
    private Timestamp startTime;

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

    public String getRankID() {
        return rankID;
    }

    public void setRankID(String rankID) {
        this.rankID = rankID;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }
}