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
import com.kingpivot.base.memberRank.dao.MemberRankDao;
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
    private MemberRankDao memberRankDao;
    @Autowired
    private CartGoodsDao cartGoodsDao;
    @Autowired
    private MemberBonusDao memberBonusDao;
    @Autowired
    private KingBase kingBase;

    @Override
    public BaseDao<MemberOrder, String> getDAO() {
        return this.memberOrderDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createMemberOrder(Member member, GoodsShop goodsShop, String objectFeatureItemID1,
                                    int qty, String contactName, String contactPhone, String address,
                                    String memberBonusID) {
        Double rate = memberRankDao.getDepositeRateByMemberId(member.getId());
        if (rate == null) {
            rate = 1d;
        }

        MemberOrder memberOrder = new MemberOrder();
        memberOrder.setOrderCode(sequenceDefineService.genCode("orderSeq", memberOrder.getId()));
        memberOrder.setApplicationID(member.getApplicationID());
        memberOrder.setMemberID(member.getId());
        memberOrder.setCompanyID(goodsShop.getCompanyID());
        memberOrder.setShopID(goodsShop.getShopID());
        memberOrder.setGoodsQTY(qty);
        memberOrder.setGoodsNumbers(1);
        memberOrder.setPriceStandTotal(NumberUtils.keepPrecision(goodsShop.getRealPrice() * qty, 2));
        memberOrder.setPriceTotal(NumberUtils.keepPrecision(goodsShop.getRealPrice() * qty * rate, 2));
        memberOrder.setPriceAfterDiscount(memberOrder.getPriceTotal());
        memberOrder.setBonusAmount(0d);
        memberOrder.setContactName(contactPhone);
        memberOrder.setContactPhone(contactPhone);
        memberOrder.setAddress(address);
        memberOrder.setApplyTime(new Timestamp(System.currentTimeMillis()));
        memberOrder.setCreatedTime(memberOrder.getApplyTime());
        String memberPaymentID = kingBase.addMemberPayment(member, Config.MEMBERORDER_OBJECTDEFINEID,memberOrder.getPriceAfterDiscount());
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
        memberOrder.setDiscountRate(rate);//折扣比例
        memberOrderDao.save(memberOrder);

        MemberOrderGoods memberOrderGoods = new MemberOrderGoods();
        memberOrderGoods.setGoodsShopID(goodsShop.getId());
        memberOrderGoods.setMemberOrderID(memberOrder.getId());
        memberOrderGoods.setName(goodsShop.getName());
        memberOrderGoods.setDescription(goodsShop.getDescription());
        memberOrderGoods.setDiscountRate(rate);
        memberOrderGoods.setPriceStand(goodsShop.getRealPrice());
        memberOrderGoods.setPriceStandTotal(NumberUtils.keepPrecision(goodsShop.getRealPrice() * qty, 2));
        memberOrderGoods.setPriceNow(NumberUtils.keepPrecision(goodsShop.getRealPrice() * rate, 2));
        memberOrderGoods.setPriceTotal(NumberUtils.keepPrecision(memberOrderGoods.getPriceNow() * qty, 2));
        if (StringUtils.isNotBlank(objectFeatureItemID1)) {
            memberOrderGoods.setObjectFeatureItemID1(objectFeatureItemID1);
        }
        memberOrderGoods.setPriceReturn(memberOrder.getPriceAfterDiscount());//退款金额
        memberOrderGoods.setPriceTotalReturn(memberOrder.getPriceAfterDiscount());//退款总金额
        memberOrderGoods.setQTY(qty);
        memberOrderGoods.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        memberOrderGoodsService.save(memberOrderGoods);
        return memberPaymentID;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createMemberOrderFromCart(List<CartGoods> cartGoodsList, Member member, String contactName,
                                            String contactPhone, String address,
                                            String memberBonusID) {
        double rate = 1;
        double priceStandTotal = 0d;
        double priceTotal = 0d;
        int qty = 0;
        int goodsNumbers = 0;
        String companyID = null;
        String shopID = null;
        for (CartGoods cartGoods : cartGoodsList) {
            rate = cartGoods.getDiscountRate();
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
        memberOrder.setPriceStandTotal(priceStandTotal);
        memberOrder.setPriceTotal(priceTotal);

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
        String memberPaymentID = kingBase.addMemberPayment(member, Config.MEMBERORDER_OBJECTDEFINEID,memberOrder.getPriceAfterDiscount());
        memberOrder.setMemberPaymentID(memberPaymentID);
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
            memberOrderGoods.setPriceNow(cartGoods.getPriceNow());
            memberOrderGoods.setPriceTotal(cartGoods.getPriceTotal());
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
            memberOrderGoodsService.save(memberOrderGoods);

            cartGoods.setIsValid(0);
            cartGoods.setModifiedTime(new Timestamp(System.currentTimeMillis()));
            cartGoodsDao.save(cartGoods);
        }
        return memberPaymentID;
    }

}
