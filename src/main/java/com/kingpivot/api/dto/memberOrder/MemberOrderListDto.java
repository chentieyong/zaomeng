package com.kingpivot.api.dto.memberOrder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.common.utils.TimeTest;

import java.sql.Timestamp;
import java.util.List;

public class MemberOrderListDto {
    private String id;//主键
    private String name;//名称
    private String memberID;//会员ID
    @JsonIgnore
    private Member member;
    private String memberName;
    private String avatarURL;
    private String orderCode;//订单编号
    private Timestamp applyTime;//申请时间
    private String applyTimeStr;//申请时间Str
    private Timestamp getGoodsTime;//签收时间
    private String getGoodsTimeStr;//签收时间Str
    private Integer goodsNumbers = 0;//商品种类数
    private Integer goodsQTY = 0;//商品数量
    private Double priceTotal = 0.0d;//商品总价格
    private Double priceStandTotal = 0.0;//标准总价格
    private Double sendPrice = 0.0d;//运费
    private double discountRate = 1d;//折扣比例
    private Double priceAfterDiscount = 0.0d;//优惠后总价格
    private Double payTotal = 0.0d;//实际付款金额
    private Integer payFrom;//付款方式
    private Timestamp payTime;//付款时间
    private String payTimeStr;//付款时间Str
    private String paySequence;//付款流水号
    private Integer sendType;//发货方式
    private String memberMemo;//会员留言
    private Timestamp sendTime;//发货时间
    private String sendTimeStr;//发货时间Str
    private String sendCode;//发货单号码
    private int status = 1;//订单状态
    private String contactName;//联系人
    private String contactPhone;//联系电话
    private String address;//地址
    private Double bonusAmount = 0.0d;//红包价格
    private List<MemberOrderListGoodsListDto> goodsList;

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
        if (member != null) {
            this.memberName = member.getName();
            this.avatarURL = member.getAvatarURL();
        }
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Timestamp getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Timestamp applyTime) {
        this.applyTime = applyTime;
        if(applyTime!=null){
            this.applyTimeStr = TimeTest.toDateTimeFormat(applyTime);
        }
    }

    public Timestamp getGetGoodsTime() {
        return getGoodsTime;
    }

    public void setGetGoodsTime(Timestamp getGoodsTime) {
        this.getGoodsTime = getGoodsTime;
        if(getGoodsTime!=null){
            this.getGoodsTimeStr = TimeTest.toDateTimeFormat(getGoodsTime);
        }
    }

    public Integer getGoodsNumbers() {
        return goodsNumbers;
    }

    public void setGoodsNumbers(Integer goodsNumbers) {
        this.goodsNumbers = goodsNumbers;
    }

    public Integer getGoodsQTY() {
        return goodsQTY;
    }

    public void setGoodsQTY(Integer goodsQTY) {
        this.goodsQTY = goodsQTY;
    }

    public Double getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(Double priceTotal) {
        this.priceTotal = priceTotal;
    }

    public Double getPriceStandTotal() {
        return priceStandTotal;
    }

    public void setPriceStandTotal(Double priceStandTotal) {
        this.priceStandTotal = priceStandTotal;
    }

    public Double getSendPrice() {
        return sendPrice;
    }

    public void setSendPrice(Double sendPrice) {
        this.sendPrice = sendPrice;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public Double getPriceAfterDiscount() {
        return priceAfterDiscount;
    }

    public void setPriceAfterDiscount(Double priceAfterDiscount) {
        this.priceAfterDiscount = priceAfterDiscount;
    }

    public Double getPayTotal() {
        return payTotal;
    }

    public void setPayTotal(Double payTotal) {
        this.payTotal = payTotal;
    }

    public Integer getPayFrom() {
        return payFrom;
    }

    public void setPayFrom(Integer payFrom) {
        this.payFrom = payFrom;
    }

    public Timestamp getPayTime() {
        return payTime;
    }

    public void setPayTime(Timestamp payTime) {
        this.payTime = payTime;
        if(payTime!=null){
            this.payTimeStr = TimeTest.toDateTimeFormat(payTime);
        }
    }

    public String getPaySequence() {
        return paySequence;
    }

    public void setPaySequence(String paySequence) {
        this.paySequence = paySequence;
    }

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    public String getMemberMemo() {
        return memberMemo;
    }

    public void setMemberMemo(String memberMemo) {
        this.memberMemo = memberMemo;
    }

    public Timestamp getSendTime() {
        return sendTime;
    }

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
        if(sendTime!=null){
            this.sendTimeStr = TimeTest.toDateTimeFormat(sendTime);
        }
    }

    public String getSendCode() {
        return sendCode;
    }

    public void setSendCode(String sendCode) {
        this.sendCode = sendCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public List<MemberOrderListGoodsListDto> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<MemberOrderListGoodsListDto> goodsList) {
        this.goodsList = goodsList;
    }

    public String getApplyTimeStr() {
        return applyTimeStr;
    }

    public void setApplyTimeStr(String applyTimeStr) {
        this.applyTimeStr = applyTimeStr;
    }

    public String getGetGoodsTimeStr() {
        return getGoodsTimeStr;
    }

    public void setGetGoodsTimeStr(String getGoodsTimeStr) {
        this.getGoodsTimeStr = getGoodsTimeStr;
    }

    public String getPayTimeStr() {
        return payTimeStr;
    }

    public void setPayTimeStr(String payTimeStr) {
        this.payTimeStr = payTimeStr;
    }

    public String getSendTimeStr() {
        return sendTimeStr;
    }

    public void setSendTimeStr(String sendTimeStr) {
        this.sendTimeStr = sendTimeStr;
    }

    public Double getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(Double bonusAmount) {
        this.bonusAmount = bonusAmount;
    }
}
