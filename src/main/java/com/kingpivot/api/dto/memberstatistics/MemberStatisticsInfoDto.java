package com.kingpivot.api.dto.memberstatistics;

public class MemberStatisticsInfoDto {
    private String id;//主键
    private String name;//名称
    private String memberID;//会员ID
    private String applicationID;//应用ID
    private Integer point = 0;//现实积分
    private Integer pointTotalUsed = 0;//累计消耗积分
    private Integer pointTotal = 0;//累计积分
    private Double cashBalance = 0d;//现金余额"
    private Double cashTotalRecharge = 0d;//累计充值"
    private double cashTotalIncome = 0d;//累计收入
    private int memberBonusNum;
    private int goodsShopCollectNum;
    private String rankName;
    private String rankUrl;

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

    public Integer getPointTotalUsed() {
        return pointTotalUsed;
    }

    public void setPointTotalUsed(Integer pointTotalUsed) {
        this.pointTotalUsed = pointTotalUsed;
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

    public int getMemberBonusNum() {
        return memberBonusNum;
    }

    public void setMemberBonusNum(int memberBonusNum) {
        this.memberBonusNum = memberBonusNum;
    }

    public int getGoodsShopCollectNum() {
        return goodsShopCollectNum;
    }

    public void setGoodsShopCollectNum(int goodsShopCollectNum) {
        this.goodsShopCollectNum = goodsShopCollectNum;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public String getRankUrl() {
        return rankUrl;
    }

    public void setRankUrl(String rankUrl) {
        this.rankUrl = rankUrl;
    }

    public double getCashTotalIncome() {
        return cashTotalIncome;
    }

    public void setCashTotalIncome(double cashTotalIncome) {
        this.cashTotalIncome = cashTotalIncome;
    }
}
