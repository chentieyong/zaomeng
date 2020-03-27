package com.kingpivot.base.memberRaffle.model;

import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.raffle.model.Raffle;
import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 中奖记录
 */
@Entity
@Table(name = "memberRaffle")
public class MemberRaffle extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//中奖记录id

    @Column()
    private String applicationID;//应用ID

    @Column(length = 100)
    private String name;//名称

    @Column(length = 36)
    private String raffleID;//奖品id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raffleID", insertable = false, updatable = false)  //不能保存和修改
    private Raffle raffle;

    @Column(length = 36)
    private String memberID;//会员ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberID", insertable = false, updatable = false)  //不能保存和修改
    private Member member;

    @Column(name = "giveType", columnDefinition = "int default 1")
    private int giveType = 1;//奖励发放形式 1自提  2快递

    @Column()
    private Timestamp applyDoorTime;//预约领取时间

    @Column(length = 50)
    private String contactName;//联系人

    @Column(length = 11)
    private String contactPhone;//联系电话

    @Column(length = 100)
    private String address;//地址

    @Column(length = 36)
    private String giveUserID;//发放人UserID

    @Column()
    private Timestamp giveTime;//发放时间

    @Column(length = 100)
    private String deliveryCode;//快递单号

    @Column(name = "status", columnDefinition = "int default 1")
    private int status = 1;//1待发放 2已发放

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

    public String getRaffleID() {
        return raffleID;
    }

    public void setRaffleID(String raffleID) {
        this.raffleID = raffleID;
    }

    public Raffle getRaffle() {
        return raffle;
    }

    public void setRaffle(Raffle raffle) {
        this.raffle = raffle;
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

    public int getGiveType() {
        return giveType;
    }

    public void setGiveType(int giveType) {
        this.giveType = giveType;
    }

    public Timestamp getApplyDoorTime() {
        return applyDoorTime;
    }

    public void setApplyDoorTime(Timestamp applyDoorTime) {
        this.applyDoorTime = applyDoorTime;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGiveUserID() {
        return giveUserID;
    }

    public void setGiveUserID(String giveUserID) {
        this.giveUserID = giveUserID;
    }

    public Timestamp getGiveTime() {
        return giveTime;
    }

    public void setGiveTime(Timestamp giveTime) {
        this.giveTime = giveTime;
    }

    public String getDeliveryCode() {
        return deliveryCode;
    }

    public void setDeliveryCode(String deliveryCode) {
        this.deliveryCode = deliveryCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

