package com.kingpivot.base.raffle.model;

import com.kingpivot.base.goodsShop.model.GoodsShop;
import com.kingpivot.base.lottery.model.Lottery;
import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 奖品
 */
@Entity
@Table(name = "raffle")
public class Raffle extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//奖品id

    @Column()
    private String applicationID;//应用ID

    @Column(length = 100)
    private String name;//奖品名称

    @Column(length = 36)
    private String lotteryID;//抽奖
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lotteryID", insertable = false, updatable = false)  //不能保存和修改
    private Lottery lottery;

    @Column(length = 11)
    private int orderSeq;//序号

    @Column(length = 100)
    private String listImage;//列表图

    @Column(length = 100)
    private String faceImage;//押题图

    @Column(name = "point", columnDefinition = "int default 0")
    private int point = 0;//积分个数

    @Column(name = "cash", columnDefinition = "double default 0")
    private double cash = 0d;//钱包金额

    @Column(length = 36)
    private String goodsShopID;//实物商品
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goodsShopID", insertable = false, updatable = false)  //不能保存和修改
    private GoodsShop goodsShop;

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

    public int getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(int orderSeq) {
        this.orderSeq = orderSeq;
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

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public String getGoodsShopID() {
        return goodsShopID;
    }

    public void setGoodsShopID(String goodsShopID) {
        this.goodsShopID = goodsShopID;
    }

    public GoodsShop getGoodsShop() {
        return goodsShop;
    }

    public void setGoodsShop(GoodsShop goodsShop) {
        this.goodsShop = goodsShop;
    }
}

