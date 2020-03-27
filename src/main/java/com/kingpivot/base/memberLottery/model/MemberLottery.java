package com.kingpivot.base.memberLottery.model;

import com.kingpivot.base.lottery.model.Lottery;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 抽奖记录
 */
@Entity
@Table(name = "memberLottery")
public class MemberLottery extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//抽奖记录ID

    @Column(length = 30)
    private String name;

    @Column(length = 36)
    private String applicationID;//应用ID

    @Column(length = 36)
    private String lotteryID;//抽奖ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lotteryID", insertable = false, updatable = false)  //不能保存和修改
    private Lottery lottery;

    @Column(length = 36)
    private String memberID;//会员ID"
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberID", insertable = false, updatable = false)  //不能保存和修改
    private Member member;

    @Column()
    private Timestamp joinTime;//参加时间"

    @Column(length = 10)
    private String myCode;//抽奖码"

    @Column()
    private Timestamp giveTime;//发奖时间

    @Column(name = "resultType", columnDefinition = "int default 1")
    private int resultType = 1;//结果 1待开奖 2已中奖 3未中奖

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

    public String getLotteryID() {
        return lotteryID;
    }

    public void setLotteryID(String lotteryID) {
        this.lotteryID = lotteryID;
    }

    public Lottery getLottery() {
        return lottery;
    }

    public void setLottery(Lottery lottery) {
        this.lottery = lottery;
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

    public Timestamp getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Timestamp joinTime) {
        this.joinTime = joinTime;
    }

    public String getMyCode() {
        return myCode;
    }

    public void setMyCode(String myCode) {
        this.myCode = myCode;
    }

    public Timestamp getGiveTime() {
        return giveTime;
    }

    public void setGiveTime(Timestamp giveTime) {
        this.giveTime = giveTime;
    }

    public int getResultType() {
        return resultType;
    }

    public void setResultType(int resultType) {
        this.resultType = resultType;
    }
}

