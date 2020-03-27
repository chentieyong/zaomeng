package com.kingpivot.base.lotteryGrade.model;

import com.kingpivot.base.lottery.model.Lottery;
import com.kingpivot.base.raffle.model.Raffle;
import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 抽奖等级表
 */
@Entity
@Table(name = "lotteryGrade")
public class LotteryGrade extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//抽奖等级表ID

    @Column()
    private String applicationID;//应用ID

    @Column(length = 36)
    private String lotteryID;//抽奖ID"
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lotteryID", insertable = false, updatable = false)  //不能保存和修改
    private Lottery lottery;

    @Column(length = 100)
    private String name;//名称"

    @Column(length = 100)
    private String shortName;//简称"

    @Column(name = "orderSeq", columnDefinition = "int default 1")
    private int orderSeq = 1;//排序号"

    @Column(length = 500)
    private String description;//描述"

    @Column(name = "joinGotTimes", columnDefinition = "int default 1")
    private int joinGotTimes = 1;//每人中奖次数

    @Column(name = "number", columnDefinition = "int default 1")
    private int number = 1;//个数"

    @Column(name = "getNumber", columnDefinition = "int default 0")
    private int getNumber = 0;//实际个数

    @Column(name = "raffleRate", columnDefinition = "int default 1")
    private int raffleRate = 1;//中奖比例

    @Column(length = 36)
    private String raffleID;//奖励ID"
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raffleID", insertable = false, updatable = false)  //不能保存和修改
    private Raffle raffle;

    @Column(length = 100)
    private String listImage;//列表图"

    @Column(length = 100)
    private String faceImage;//压题图"

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

    public int getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(int orderSeq) {
        this.orderSeq = orderSeq;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getJoinGotTimes() {
        return joinGotTimes;
    }

    public void setJoinGotTimes(int joinGotTimes) {
        this.joinGotTimes = joinGotTimes;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getGetNumber() {
        return getNumber;
    }

    public void setGetNumber(int getNumber) {
        this.getNumber = getNumber;
    }

    public int getRaffleRate() {
        return raffleRate;
    }

    public void setRaffleRate(int raffleRate) {
        this.raffleRate = raffleRate;
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

    public String getListImage() {
        return listImage;
    }

    public void setListImage(String listImage) {
        this.listImage = listImage;
    }

    public String getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }

    @Override
    public String toString() {
        return "LotteryGrade{" +
                "id='" + id + '\'' +
                ", applicationID='" + applicationID + '\'' +
                ", lotteryID='" + lotteryID + '\'' +
                ", lottery=" + lottery +
                ", name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                ", orderSeq=" + orderSeq +
                ", description='" + description + '\'' +
                ", joinGotTimes=" + joinGotTimes +
                ", number=" + number +
                ", getNumber=" + getNumber +
                ", raffleRate=" + raffleRate +
                ", raffleID='" + raffleID + '\'' +
                ", raffle=" + raffle +
                ", listImage='" + listImage + '\'' +
                ", faceImage='" + faceImage + '\'' +
                '}';
    }
}

