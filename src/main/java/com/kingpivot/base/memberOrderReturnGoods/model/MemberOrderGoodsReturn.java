package com.kingpivot.base.memberOrderReturnGoods.model;

import com.kingpivot.base.goodsShop.model.GoodsShop;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.objectFeatureItem.model.ObjectFeatureItem;
import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "memberOrderGoodsReturn")
public class MemberOrderGoodsReturn extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键
    @Column(length = 100)
    private String name;
    @Column(length = 36)
    private String memberOrderGoodsID;//会员订单商品ID
    @Column(length = 36)
    private String paywayID;//支付机构id
    @Column(length = 36)
    private String memberID;//会员ID
    @Column(length = 36)
    private String applicationID;//应用ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberID", insertable = false, updatable = false)//不能保存和修改
    private Member member;
    @Column(length = 20)
    private String returnCode;//退货单号
    @Column(length = 2000)
    private String description;//退款说明
    @Column()
    private Timestamp applyTime;//申请时间
    @Column(length = 36)
    private String goodsShopID;//商品店铺ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goodsShopID", insertable = false, updatable = false)//不能保存和修改
    private GoodsShop goodsShop;
    @Column(length = 36)
    private String objectFeatureItemID1; //对象特征选项ID1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "objectFeatureItemID1", updatable = false, insertable = false)
    private ObjectFeatureItem objectFeatureItem1;
    @Column()
    private Double priceTotalReturn = 0.0;//退款总价格
    @Column()
    private Integer qty = 0;//退货数量
    @Column(name = "status", columnDefinition = "int default 1")
    private Integer status = 1;//状态，1新，2已退货，3已拒绝
    @Column(length = 100)
    private String memo;//审核说明

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

    public String getMemberOrderGoodsID() {
        return memberOrderGoodsID;
    }

    public void setMemberOrderGoodsID(String memberOrderGoodsID) {
        this.memberOrderGoodsID = memberOrderGoodsID;
    }

    public String getPaywayID() {
        return paywayID;
    }

    public void setPaywayID(String paywayID) {
        this.paywayID = paywayID;
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

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Timestamp applyTime) {
        this.applyTime = applyTime;
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

    public String getObjectFeatureItemID1() {
        return objectFeatureItemID1;
    }

    public void setObjectFeatureItemID1(String objectFeatureItemID1) {
        this.objectFeatureItemID1 = objectFeatureItemID1;
    }

    public ObjectFeatureItem getObjectFeatureItem1() {
        return objectFeatureItem1;
    }

    public void setObjectFeatureItem1(ObjectFeatureItem objectFeatureItem1) {
        this.objectFeatureItem1 = objectFeatureItem1;
    }

    public Double getPriceTotalReturn() {
        return priceTotalReturn;
    }

    public void setPriceTotalReturn(Double priceTotalReturn) {
        this.priceTotalReturn = priceTotalReturn;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
