package com.kingpivot.base.memberOrder.service.impl;

import com.kingpivot.base.cartGoods.dao.CartGoodsDao;
import com.kingpivot.base.cartGoods.model.CartGoods;
import com.kingpivot.base.config.Config;
import com.kingpivot.base.goodsShop.model.GoodsShop;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.memberBonus.dao.MemberBonusDao;
import com.kingpivot.base.memberBonus.model.MemberBonus;
import com.kingpivot.base.memberOrder.dao.MemberOrderDao;
import com.kingpivot.base.memberOrder.model.MemberOrder;
import com.kingpivot.base.memberOrder.service.MemberOrderService;
import com.kingpivot.base.memberOrderGoods.model.MemberOrderGoods;
import com.kingpivot.base.memberOrderGoods.service.MemberOrderGoodsService;
import com.kingpivot.base.objectFeatureData.dao.ObjectFeatureDataDao;
import com.kingpivot.base.rank.dao.RankDao;
import com.kingpivot.base.rank.model.Rank;
import com.kingpivot.base.sequenceDefine.service.SequenceDefineService;
import com.kingpivot.common.KingBase;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import com.kingpivot.common.utils.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Administrator on 2016/9/18.
 */
@Service("memberOrderService")
public class MemberOrderServiceImpl extends BaseServiceImpl<MemberOrder, String> implements MemberOrderService {
    @Resource(name = "memberOrderDao")
    private MemberOrderDao memberOrderDao;
    @Autowired
    private SequenceDefineService sequenceDefineService;
    @Autowired
    private MemberOrderGoodsService memberOrderGoodsService;
    @Autowired
    private CartGoodsDao cartGoodsDao;
    @Autowired
    private MemberBonusDao memberBonusDao;
    @Autowired
    private KingBase kingBase;
    @Autowired
    private ObjectFeatureDataDao objectFeatureDataDao;
    @Autowired
    private RankDao rankDao;

    @Override
    public BaseDao<MemberOrder, String> getDAO() {
        return this.memberOrderDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createMemberOrder(Member member, GoodsShop goodsShop, String objectFeatureItemID1,
                                    int qty, String contactName, String contactPhone, String address,
                                    String memberBonusID) {
        double price = 0d;
        if (StringUtils.isNotBlank(objectFeatureItemID1)) {
            Object[] objectFeatureDataDto = objectFeatureDataDao.getObjectFetureData(goodsShop.getId(), objectFeatureItemID1);
            if (objectFeatureDataDto != null && objectFeatureDataDto.length != 0) {
                Double val = (Double) objectFeatureDataDto[0];
                if (val != null && val != 0) {
                    price = val;
                }
            }
        }

        double rate = 1d;
        if (StringUtils.isNotBlank(member.getRankID())) {
            Rank rank = rankDao.findOne(member.getRankID());
            if (rank != null && rank.getDepositeRate() != null && rank.getDepositeRate() != 0) {
                rate = price * rank.getDepositeRate().doubleValue();
            }
        }

        MemberOrder memberOrder = new MemberOrder();
        memberOrder.setOrderCode(sequenceDefineService.genCode("orderSeq", memberOrder.getId()));
        memberOrder.setApplicationID(member.getApplicationID());
        memberOrder.setMemberID(member.getId());
        memberOrder.setCompanyID(goodsShop.getCompanyID());
        memberOrder.setShopID(goodsShop.getShopID());
        memberOrder.setGoodsQTY(qty);
        memberOrder.setGoodsNumbers(1);
        memberOrder.setDiscountRate(rate);
        memberOrder.setPriceStandTotal(NumberUtils.keepPrecision(price * qty, 2));
        memberOrder.setPriceTotal(NumberUtils.keepPrecision(price * qty * rate, 2));
        memberOrder.setPriceAfterDiscount(memberOrder.getPriceTotal());
        memberOrder.setBonusAmount(0d);
        memberOrder.setContactName(contactPhone);
        memberOrder.setContactPhone(contactPhone);
        memberOrder.setAddress(address);
        memberOrder.setApplyTime(new Timestamp(System.currentTimeMillis()));
        String memberPaymentID = kingBase.addMemberPayment(member, Config.MEMBERORDER_OBJECTDEFINEID, memberOrder.getPriceAfterDiscount());
        memberOrder.setMemberPaymentID(memberPaymentID);

        if (StringUtils.isNotBlank(memberBonusID)) {
            MemberBonus memberBonus = memberBonusDao.findOne(memberBonusID);
            if (memberBonus != null) {
                memberOrder.setBonusAmount(memberBonus.getAmount());//红包金额
                memberOrder.setPriceAfterDiscount(NumberUtils.keepPrecision(memberOrder.getPriceAfterDiscount() - memberBonus.getAmount(), 2));//优惠后金额

                memberBonus.setUseTime(new Timestamp(System.currentTimeMillis()));
                memberBonus.setMemberOrderID(memberOrder.getId());
                memberBonusDao.save(memberBonus);
            }
        }
        memberOrder.setCreatedTime(memberOrder.getApplyTime());
        memberOrder.setModifiedTime(memberOrder.getApplyTime());
        memberOrderDao.save(memberOrder);

        MemberOrderGoods memberOrderGoods = new MemberOrderGoods();
        memberOrderGoods.setGoodsShopID(goodsShop.getId());
        memberOrderGoods.setMemberOrderID(memberOrder.getId());
        memberOrderGoods.setName(goodsShop.getName());
        memberOrderGoods.setDescription(goodsShop.getDescription());
        memberOrderGoods.setDiscountRate(rate);
        memberOrderGoods.setPriceStand(goodsShop.getRealPrice());
        memberOrderGoods.setPriceStandTotal(NumberUtils.keepPrecision(price * qty, 2));
        memberOrderGoods.setPriceNow(NumberUtils.keepPrecision(price * rate, 2));
        memberOrderGoods.setPriceTotal(NumberUtils.keepPrecision(memberOrderGoods.getPriceNow() * qty, 2));
        if (StringUtils.isNotBlank(objectFeatureItemID1)) {
            memberOrderGoods.setObjectFeatureItemID1(objectFeatureItemID1);
        }
        memberOrderGoods.setPriceReturn(memberOrder.getPriceAfterDiscount());//退款金额
        memberOrderGoods.setPriceTotalReturn(memberOrder.getPriceAfterDiscount());//退款总金额
        memberOrderGoods.setQTY(qty);
        memberOrderGoods.setIsReturn(goodsShop.getIsReturn());
        memberOrderGoods.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        memberOrderGoodsService.save(memberOrderGoods);
        return memberPaymentID;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createMemberOrderFromCart(List<CartGoods> cartGoodsList, Member member, String contactName,
                                            String contactPhone, String address,
                                            String memberBonusID) {
        double rate = 1d;
        if (StringUtils.isNotBlank(member.getRankID())) {
            Rank rank = rankDao.findOne(member.getRankID());
            if (rank != null && rank.getDepositeRate() != null && rank.getDepositeRate() != 0) {
                rate = rank.getDepositeRate().doubleValue();
            }
        }

        double priceStandTotal = 0d;
        double priceTotal = 0d;
        int qty = 0;
        int goodsNumbers = 0;
        String companyID = null;
        String shopID = null;
        for (CartGoods cartGoods : cartGoodsList) {
            qty += cartGoods.getQty();
            priceStandTotal += cartGoods.getStandPriceTotal();
            priceTotal += cartGoods.getPriceTotal();
            companyID = cartGoods.getCompanyID();
            shopID = cartGoods.getShopID();
        }
        goodsNumbers = cartGoodsList.size();

        MemberOrder memberOrder = new MemberOrder();
        memberOrder.setOrderCode(sequenceDefineService.genCode("orderSeq", memberOrder.getId()));
        memberOrder.setApplicationID(member.getApplicationID());
        memberOrder.setMemberID(member.getId());
        memberOrder.setCompanyID(companyID);
        memberOrder.setShopID(shopID);
        memberOrder.setGoodsQTY(qty);
        memberOrder.setGoodsNumbers(goodsNumbers);
        memberOrder.setPriceStandTotal(NumberUtils.keepPrecision(priceStandTotal, 2));
        memberOrder.setPriceTotal(NumberUtils.keepPrecision(priceTotal * rate, 2));
        memberOrder.setPriceAfterDiscount(memberOrder.getPriceTotal());//优惠后金额

        if (StringUtils.isNotBlank(memberBonusID)) {
            MemberBonus memberBonus = memberBonusDao.findOne(memberBonusID);
            if (memberBonus != null) {
                memberOrder.setBonusAmount(memberBonus.getAmount());//红包金额
                memberOrder.setPriceAfterDiscount(NumberUtils.keepPrecision(memberOrder.getPriceAfterDiscount() - memberBonus.getAmount(), 2));//优惠后金额

                memberBonus.setUseTime(new Timestamp(System.currentTimeMillis()));
                memberBonus.setMemberOrderID(memberOrder.getId());
                memberBonusDao.save(memberBonus);
            }
        }
        memberOrder.setDiscountRate(rate);//折扣比例

        memberOrder.setContactName(contactPhone);
        memberOrder.setContactPhone(contactPhone);
        memberOrder.setAddress(address);
        memberOrder.setApplyTime(new Timestamp(System.currentTimeMillis()));
        memberOrder.setCreatedTime(memberOrder.getApplyTime());
        String memberPaymentID = kingBase.addMemberPayment(member, Config.MEMBERORDER_OBJECTDEFINEID, memberOrder.getPriceAfterDiscount());
        memberOrder.setMemberPaymentID(memberPaymentID);
        memberOrder.setModifiedTime(memberOrder.getApplyTime());
        memberOrderDao.save(memberOrder);

        for (CartGoods cartGoods : cartGoodsList) {
            MemberOrderGoods memberOrderGoods = new MemberOrderGoods();
            memberOrderGoods.setGoodsShopID(cartGoods.getGoodsShopID());
            memberOrderGoods.setMemberOrderID(memberOrder.getId());
            memberOrderGoods.setName(cartGoods.getName());
            memberOrderGoods.setDescription(cartGoods.getDescription());
            memberOrderGoods.setDiscountRate(rate);
            memberOrderGoods.setPriceStand(cartGoods.getStandPrice());
            memberOrderGoods.setPriceStandTotal(cartGoods.getStandPriceTotal());
            memberOrderGoods.setPriceNow(cartGoods.getPriceNow() * rate);
            memberOrderGoods.setPriceTotal(cartGoods.getPriceTotal() * rate);
            memberOrderGoods.setObjectFeatureItemID1(cartGoods.getObjectFeatureItemID1());
            memberOrderGoods.setQTY(cartGoods.getQty());
            memberOrderGoods.setCreatedTime(new Timestamp(System.currentTimeMillis()));
            memberOrderGoods.setPriceTotalReturn(memberOrderGoods.getPriceTotal());//退款总金额
            memberOrderGoods.setPriceReturn(memberOrderGoods.getPriceNow());//退款金额
            if (memberOrder.getBonusAmount() != null && memberOrder.getBonusAmount() != 0) {
                double priceTotalReturn = memberOrderGoods.getPriceTotal() / priceTotal * memberOrder.getBonusAmount();
                memberOrderGoods.setPriceTotalReturn(NumberUtils.keepPrecision(memberOrderGoods.getPriceReturn() -
                        priceTotalReturn, 2));
                memberOrderGoods.setPriceReturn(NumberUtils.keepPrecision(memberOrderGoods.getPriceTotal() /
                        memberOrderGoods.getQTY(), 2));
            }
            memberOrderGoods.setIsReturn(cartGoods.getGoodsShop().getIsReturn());
            memberOrderGoodsService.save(memberOrderGoods);

            cartGoods.setIsValid(0);
            cartGoods.setModifiedTime(new Timestamp(System.currentTimeMillis()));
            cartGoodsDao.save(cartGoods);
        }
        return memberPaymentID;
    }

    @Override
    public List<MemberOrder> getMemberOrderByMemberPayMentID(String memberPaymentID) {
        return memberOrderDao.getMemberOrderByMemberPayMentID(memberPaymentID);
    }

    @Override
    public void updateMemberOrderByMemberPaymentID(String paywayID, String memberPaymentID) {
        memberOrderDao.updateMemberOrderByMemberPaymentID(paywayID, memberPaymentID);
    }
}
