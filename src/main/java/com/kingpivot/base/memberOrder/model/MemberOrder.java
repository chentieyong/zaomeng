package com.kingpivot.base.memberOrder.model;

import com.kingpivot.base.member.model.Member;
import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "memberOrder")
public class MemberOrder extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键

    @Column(length = 100)
    private String name;//名称

    @Column(length = 36)
    private String applicationID;//应用ID

    @Column(length = 36)
    private String memberID;//会员ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MemberID", insertable = false, updatable = false)  //不能保存和修改
    private Member member;

    @Column(length = 20)
    private String orderCode;//订单编号

    @Column()
    private Integer orderType = 1;//订单类型

    @Column()
    private Timestamp applyTime;//申请时间

    @Column()
    private Timestamp getGoodsTime;//签收时间

    @Column()
    private Timestamp cancelTime;//取消时间

    @Column()
    private Integer goodsNumbers = 0;//商品种类数

    @Column()
    private Integer goodsQTY = 0;//商品数量

    @Column()
    private Double priceTotal = 0.0d;//商品总价格

    @Column()
    private Double priceStandTotal = 0.0;//标准总价格

    @Column
    private Double sendPrice = 0.0d;//运费

    @Column()
    private double discountRate = 1d;//折扣比例

    @Column()
    private Double bonusAmount = 0.0d;//红包价格

    @Column()
    private Double priceAfterDiscount = 0.0d;//优惠后总价格

    @Column()
    private Double payTotal = 0.0d;//实际付款金额

    @Column()
    private Integer payFrom=1;//付款方式 默认第三方支付

    @Column()
    private Timestamp payTime;//付款时间

    @Column(length = 36)
    private String paywayID;//支付机构ID

    @Column(length = 100)
    private String paySequence;//付款流水号

    @Column()
    private Integer returnType;//退款类型

    @Column()
    private Timestamp returnTime;//退款时间

    @Column()
    private Double returnAmount = 0.0d;//退款金额

    @Column(length = 36)
    private String returnPaywayID;//退款ID

    @Column(length = 100)
    private String returnSequence;//退款流水号

    @Column()
    private Integer sendType;//发货方式

    @Column(length = 100)
    private String memberMemo;//会员留言

    @Column()
    private Timestamp processTime;//订单处理时间

    @Column()
    private Timestamp sendTime;//发货时间

    @Column(length = 20)
    private String sendCode;//发货单号码

    @Column()
    private int status = 1;//订单状态

    @Column(length = 50)
    private String contactName;//联系人

    @Column(length = 11)
    private String contactPhone;//联系电话

    @Column(length = 100)
    private String address;//地址

    @Column(length = 36)
    private String companyID;//公司ID

    @Column(length = 36)
    private String shopID;//销售店铺ID

    @Column(length = 36)
    private String memberPaymentID;//会员支付id

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

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Timestamp getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Timestamp applyTime) {
        this.applyTime = applyTime;
    }

    public Timestamp getGetGoodsTime() {
        return getGoodsTime;
    }

    public void setGetGoodsTime(Timestamp getGoodsTime) {
        this.getGoodsTime = getGoodsTime;
    }

    public Timestamp getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Timestamp cancelTime) {
        this.cancelTime = cancelTime;
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

    public Integer getReturnType() {
        return returnType;
    }

    public void setReturnType(Integer returnType) {
        this.returnType = returnType;
    }

    public Timestamp getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Timestamp returnTime) {
        this.returnTime = returnTime;
    }

    public Double getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(Double returnAmount) {
        this.returnAmount = returnAmount;
    }

    public String getReturnPaywayID() {
        return returnPaywayID;
    }

    public void setReturnPaywayID(String returnPaywayID) {
        this.returnPaywayID = returnPaywayID;
    }

    public String getReturnSequence() {
        return returnSequence;
    }

    public void setReturnSequence(String returnSequence) {
        this.returnSequence = returnSequence;
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

    public Timestamp getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Timestamp processTime) {
        this.processTime = processTime;
    }

    public Timestamp getSendTime() {
        return sendTime;
    }

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
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

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public Double getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(Double bonusAmount) {
        this.bonusAmount = bonusAmount;
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

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public String getMemberPaymentID() {
        return memberPaymentID;
    }

    public void setMemberPaymentID(String memberPaymentID) {
        this.memberPaymentID = memberPaymentID;
    }
}
