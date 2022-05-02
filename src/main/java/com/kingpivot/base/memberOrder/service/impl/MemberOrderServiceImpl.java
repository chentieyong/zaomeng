package com.kingpivot.base.memberOrder.service.impl;

import com.kingpivot.base.cartGoods.dao.CartGoodsDao;
import com.kingpivot.base.cartGoods.model.CartGoods;
import com.kingpivot.base.config.Config;
import com.kingpivot.base.goodsShop.dao.GoodsShopDao;
import com.kingpivot.base.goodsShop.model.GoodsShop;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.memberBonus.dao.MemberBonusDao;
import com.kingpivot.base.memberBonus.model.MemberBonus;
import com.kingpivot.base.memberBonus.service.MemberBonusService;
import com.kingpivot.base.memberOrder.dao.MemberOrderDao;
import com.kingpivot.base.memberOrder.model.MemberOrder;
import com.kingpivot.base.memberOrder.service.MemberOrderService;
import com.kingpivot.base.memberOrderGoods.dao.MemberOrderGoodsDao;
import com.kingpivot.base.memberOrderGoods.model.MemberOrderGoods;
import com.kingpivot.base.memberOrderGoods.service.MemberOrderGoodsService;
import com.kingpivot.base.memberPayment.dao.MemberPaymentDao;
import com.kingpivot.base.memberPayment.model.MemberPayment;
import com.kingpivot.base.memberstatistics.dao.MemberStatisticsDao;
import com.kingpivot.base.memberstatistics.model.MemberStatistics;
import com.kingpivot.base.objectFeatureData.dao.ObjectFeatureDataDao;
import com.kingpivot.base.objectFeatureData.model.ObjectFeatureData;
import com.kingpivot.base.parameter.dao.ParameterDao;
import com.kingpivot.base.point.dao.PointDao;
import com.kingpivot.base.point.model.Point;
import com.kingpivot.base.pointapplication.model.PointApplication;
import com.kingpivot.base.pointdefine.model.PointDefine;
import com.kingpivot.base.rank.dao.RankDao;
import com.kingpivot.base.rank.model.Rank;
import com.kingpivot.base.sequenceDefine.service.SequenceDefineService;
import com.kingpivot.base.shop.dao.ShopDao;
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
    private MemberOrderGoodsDao memberOrderGoodsDao;
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
    @Autowired
    private GoodsShopDao goodsShopDao;
    @Autowired
    private MemberPaymentDao memberPaymentDao;
    @Autowired
    private ShopDao shopDao;
    @Autowired
    private ParameterDao parameterDao;
    @Autowired
    private MemberStatisticsDao memberStatisticsDao;
    @Autowired
    private PointDao pointDao;

    @Override
    public BaseDao<MemberOrder, String> getDAO() {
        return this.memberOrderDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createMemberOrder(Member member, GoodsShop goodsShop, String objectFeatureItemID1,
                                    int qty, String contactName, String contactPhone, String address,
                                    String memberBonusID, String orderType, String sendType,
                                    String pointPrice, int point, String tableNumber) {
        boolean memberPrice = kingBase.checkMemberCard(member.getId());
        double price = memberPrice ? goodsShop.getMemberPrice() : goodsShop.getRealPrice();
        if (StringUtils.isNotBlank(objectFeatureItemID1)) {
            ObjectFeatureData val = objectFeatureDataDao.getObjectFetureData(goodsShop.getId(), objectFeatureItemID1);
            if (val != null) {
                price = memberPrice ? val.getMemberPrice() : val.getRealPrice();
            }
        }

        double rate = 1d;
//        if (StringUtils.isNotBlank(member.getRankID())) {
//            Rank rank = rankDao.findOne(member.getRankID());
//            if (rank != null && rank.getDepositeRate() != null && rank.getDepositeRate() != 0) {
//                rate = price * rank.getDepositeRate().doubleValue();
//            }
//        }

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
        if ("1".equals(sendType)) {
            editFare(memberOrder);
        }
        memberOrder.setBonusAmount(0d);
        memberOrder.setContactName(contactName);
        memberOrder.setContactPhone(contactPhone);
        memberOrder.setAddress(address);
        memberOrder.setSendType(sendType == null ? 1 : Integer.parseInt(sendType));
        memberOrder.setApplyTime(new Timestamp(System.currentTimeMillis()));
        memberOrder.setTableNumber(tableNumber);

        MemberBonus memberBonus = null;
        if (StringUtils.isNotBlank(memberBonusID)) {
            memberBonus = memberBonusDao.findOne(memberBonusID);
            if (memberBonus != null) {
                memberOrder.setBonusAmount(memberBonus.getAmount());//红包金额
                memberOrder.setPriceAfterDiscount(NumberUtils.keepPrecision(memberOrder.getPriceAfterDiscount() - memberBonus.getAmount(), 2));//优惠后金额
            }
        }
        if (StringUtils.isNotBlank(pointPrice)) {
            memberOrder.setPointPrice(Double.parseDouble(pointPrice));
            memberOrder.setPoint(point);
            //扣除积分
            delPoint(member, memberOrder.getPoint());
            memberOrder.setPriceAfterDiscount(NumberUtils.keepPrecision(memberOrder.getPriceAfterDiscount() - Double.parseDouble(pointPrice), 2));//优惠后金额
        }
        memberOrder.setCreatedTime(memberOrder.getApplyTime());
        memberOrder.setModifiedTime(memberOrder.getApplyTime());
        if (StringUtils.isNotBlank(orderType)) {
            memberOrder.setOrderType(Integer.parseInt(orderType));
        }
        memberOrderDao.save(memberOrder);

        if (memberBonus != null) {
            memberBonus.setStatus(2);
            memberBonus.setUseTime(new Timestamp(System.currentTimeMillis()));
            memberBonus.setMemberOrderID(memberOrder.getId());
            memberBonusDao.save(memberBonus);
        }

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
        memberOrderGoodsDao.save(memberOrderGoods);

        goodsShop.setStockOut(qty + goodsShop.getStockOut());
        goodsShop.setStockNumber(goodsShop.getStockNumber() - qty);
        goodsShopDao.save(goodsShop);

        return memberOrder.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createMemberOrderFromCart(List<CartGoods> cartGoodsList, Member member, String contactName,
                                            String contactPhone, String address,
                                            String memberBonusID, String sendType,
                                            String pointPrice, int point, String tableNumber) {
        double rate = 1d;
//        if (StringUtils.isNotBlank(member.getRankID())) {
//            Rank rank = rankDao.findOne(member.getRankID());
//            if (rank != null && rank.getDepositeRate() != null && rank.getDepositeRate() != 0) {
//                rate = rank.getDepositeRate().doubleValue();
//            }
//        }

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
        if ("1".equals(sendType)) {
            editFare(memberOrder);
        }

        MemberBonus memberBonus = null;
        if (StringUtils.isNotBlank(memberBonusID)) {
            memberBonus = memberBonusDao.findOne(memberBonusID);
            if (memberBonus != null) {
                memberOrder.setBonusAmount(memberBonus.getAmount());//红包金额
                memberOrder.setPriceAfterDiscount(NumberUtils.keepPrecision(memberOrder.getPriceAfterDiscount() - memberBonus.getAmount(), 2));//优惠后金额
            }
        }
        memberOrder.setDiscountRate(rate);//折扣比例
        memberOrder.setContactName(contactName);
        memberOrder.setContactPhone(contactPhone);
        memberOrder.setAddress(address);
        memberOrder.setSendType(sendType == null ? 1 : Integer.parseInt(sendType));
        memberOrder.setApplyTime(new Timestamp(System.currentTimeMillis()));
        memberOrder.setCreatedTime(memberOrder.getApplyTime());
        memberOrder.setModifiedTime(memberOrder.getApplyTime());
        if (StringUtils.isNotBlank(pointPrice)) {
            memberOrder.setPointPrice(Double.parseDouble(pointPrice));
            memberOrder.setPoint(point);
            //扣除积分
            delPoint(member, memberOrder.getPoint());
            memberOrder.setPriceAfterDiscount(NumberUtils.keepPrecision(memberOrder.getPriceAfterDiscount() - Double.parseDouble(pointPrice), 2));//优惠后金额
        }
        memberOrder.setTableNumber(tableNumber);
        memberOrderDao.save(memberOrder);

        if (memberBonus != null) {
            memberBonus.setStatus(2);
            memberBonus.setUseTime(new Timestamp(System.currentTimeMillis()));
            memberBonus.setMemberOrderID(memberOrder.getId());
            memberBonusDao.save(memberBonus);
        }
        GoodsShop goodsShop = null;
        for (CartGoods cartGoods : cartGoodsList) {
            MemberOrderGoods memberOrderGoods = new MemberOrderGoods();
            memberOrderGoods.setGoodsShopID(cartGoods.getGoodsShopID());
            memberOrderGoods.setMemberOrderID(memberOrder.getId());
            memberOrderGoods.setName(cartGoods.getName());
            memberOrderGoods.setDescription(cartGoods.getDescription());
            memberOrderGoods.setDiscountRate(rate);
            memberOrderGoods.setPriceStand(cartGoods.getStandPrice());
            memberOrderGoods.setPriceStandTotal(cartGoods.getStandPriceTotal());
            memberOrderGoods.setPriceNow(NumberUtils.keepPrecision(cartGoods.getPriceNow() * rate, 2));
            memberOrderGoods.setPriceTotal(NumberUtils.keepPrecision(cartGoods.getPriceTotal() * rate, 2));
            memberOrderGoods.setObjectFeatureItemID1(cartGoods.getObjectFeatureItemID1());
            memberOrderGoods.setQTY(cartGoods.getQty());
            memberOrderGoods.setCreatedTime(new Timestamp(System.currentTimeMillis()));
            memberOrderGoods.setPriceTotalReturn(memberOrderGoods.getPriceTotal());//退款总金额
            memberOrderGoods.setPriceReturn(memberOrderGoods.getPriceNow());//退款金额
            if (memberOrder.getBonusAmount() != null && memberOrder.getBonusAmount() != 0) {
                double priceTotalReturn = memberOrderGoods.getPriceTotal() / priceTotal * memberOrder.getBonusAmount();
                memberOrderGoods.setPriceTotalReturn(NumberUtils.keepPrecision(memberOrderGoods.getPriceTotalReturn() -
                        priceTotalReturn, 2));
                memberOrderGoods.setPriceReturn(NumberUtils.keepPrecision(memberOrderGoods.getPriceTotal() /
                        memberOrderGoods.getQTY(), 2));
            }
            memberOrderGoods.setIsReturn(cartGoods.getGoodsShop().getIsReturn());
            memberOrderGoodsDao.save(memberOrderGoods);

            if (cartGoods.getGoodsShop() != null) {
                goodsShop = cartGoods.getGoodsShop();
                goodsShop.setStockOut(cartGoods.getQty() + goodsShop.getStockOut());
                goodsShop.setStockNumber(goodsShop.getStockNumber() - cartGoods.getQty());
                goodsShopDao.save(goodsShop);
            }

            cartGoods.setIsValid(0);
            cartGoods.setModifiedTime(new Timestamp(System.currentTimeMillis()));
            cartGoodsDao.save(cartGoods);
        }
        return memberOrder.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createMemberOrderFromShopCart(List<String> shopList, String cartID, Member member, String contactName,
                                                String contactPhone, String address,
                                                String memberBonusID) {
        double bonusAmount = 0d;
        double bonusAmountAvg = 0d;
        MemberBonus memberBonus = null;
        if (StringUtils.isNotBlank(memberBonusID)) {
            memberBonus = memberBonusDao.findOne(memberBonusID);
            if (memberBonus != null && memberBonus.getIsValid() == 1 && memberBonus.getAmount() != 0) {
                bonusAmount = memberBonus.getAmount().doubleValue();
                bonusAmountAvg = bonusAmount / shopList.size();
            }
        }

        MemberPayment memberPayment = kingBase.addMemberPayment(member, Config.MEMBERORDER_OBJECTDEFINEID, 0d);
        List<CartGoods> cartGoodsList = null;
        MemberOrder memberOrder = null;
        double allTotal = 0d;
        for (String shopID : shopList) {
            memberOrder = new MemberOrder();
            memberOrder.setName(String.format("%s的订单", member.getName()));
            memberOrder.setApplicationID(member.getApplicationID());
            memberOrder.setMemberID(member.getId());
            memberOrder.setApplyTime(new Timestamp(System.currentTimeMillis()));
            memberOrder.setOrderCode(sequenceDefineService.genCode("orderSeq", memberOrder.getId()));
            memberOrder.setOrderType(1);
            memberOrder.setSendPrice(0d);
            memberOrder.setBonusAmount(bonusAmountAvg);
            memberOrder.setPayTotal(0d);
            memberOrder.setStatus(1);
            memberOrder.setMemberPaymentID(memberPayment.getId());
            memberOrder.setContactName(contactName);
            memberOrder.setContactPhone(contactName);
            memberOrder.setAddress(address);
            memberOrder.setCompanyID(shopDao.getCompanyIdById(shopID));
            memberOrder.setShopID(shopID);

            int goodQty = 0;
            double priceTotal = 0.0;
            cartGoodsList = cartGoodsDao.getCartGoodsListByCartID(cartID, shopID, 1);

            for (CartGoods cartGoods : cartGoodsList) {
                goodQty += cartGoods.getQty();
                priceTotal += cartGoods.getPriceTotal();
            }
            memberOrder.setGoodsNumbers(cartGoodsList.size());
            memberOrder.setGoodsQTY(goodQty);
            memberOrder.setPriceTotal(NumberUtils.keepPrecision(priceTotal, 2));

            memberOrder.setPriceAfterDiscount(NumberUtils.keepPrecision(memberOrder.getPriceTotal() - bonusAmountAvg, 2));
            editFare(memberOrder);
            allTotal += memberOrder.getPriceAfterDiscount();
            memberOrder.setCreatedTime(new Timestamp(System.currentTimeMillis()));
            memberOrderDao.save(memberOrder);

            MemberOrderGoods memberOrderGoods = null;
            for (CartGoods cartGoods : cartGoodsList) {
                memberOrderGoods = new MemberOrderGoods();
                memberOrderGoods.setMemberOrderID(memberOrder.getId());
                memberOrderGoods.setGoodsShopID(cartGoods.getGoodsShopID());
                memberOrderGoods.setName(cartGoods.getGoodsShop().getName());
                memberOrderGoods.setPriceStand(cartGoods.getStandPrice());
                memberOrderGoods.setPriceNow(cartGoods.getPriceNow());
                memberOrderGoods.setQTY(cartGoods.getQty());
                memberOrderGoods.setPriceTotal(cartGoods.getPriceTotal());
                memberOrderGoods.setPriceTotalReturn(memberOrderGoods.getPriceTotal());//退款总金额
                memberOrderGoods.setPriceReturn(memberOrderGoods.getPriceNow());//退款金额
                if (bonusAmountAvg != 0) {
                    double priceTotalReturn = memberOrderGoods.getPriceTotal() / priceTotal * bonusAmountAvg;
                    memberOrderGoods.setPriceTotalReturn(NumberUtils.keepPrecision(memberOrderGoods.getPriceTotalReturn() -
                            priceTotalReturn, 2));
                    memberOrderGoods.setPriceReturn(NumberUtils.keepPrecision(memberOrderGoods.getPriceTotal() /
                            memberOrderGoods.getQTY(), 2));
                }
                memberOrderGoods.setIsReturn(cartGoods.getGoodsShop().getIsReturn());
                memberOrderGoods.setStatus(1);
                memberOrderGoods.setCreatedTime(new Timestamp(System.currentTimeMillis()));
                memberOrderGoodsDao.save(memberOrderGoods);

                cartGoods.setIsValid(0);
                cartGoods.setModifiedTime(new Timestamp(System.currentTimeMillis()));
                cartGoodsDao.save(cartGoods);
            }
        }
        memberPayment.setAmount(allTotal);
        memberPaymentDao.save(memberPayment);

        if (memberBonus != null) {
            memberBonus.setStatus(2);
            memberBonus.setUseTime(new Timestamp(System.currentTimeMillis()));
            memberBonus.setMemberOrderID(memberOrder.getId());
            memberBonusDao.save(memberBonus);
        }
        return memberPayment.getId();
    }

    @Override
    public List<MemberOrder> getMemberOrderByMemberPayMentID(String memberPaymentID) {
        return memberOrderDao.getMemberOrderByMemberPayMentID(memberPaymentID);
    }

    @Override
    public void updateMemberOrderByMemberPaymentID(String paywayID, String memberPaymentID) {
        memberOrderDao.updateMemberOrderByMemberPaymentID(paywayID, memberPaymentID);
    }

    @Override
    public void updateMemberOrderStatus(String memberOrderID, int status) {
        memberOrderDao.updateMemberOrderStatus(memberOrderID, status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void monthBalancePayMemberOrder(MemberOrder memberOrder, MemberStatistics memberStatistics, String payWayID) {
        memberStatistics.setMonthBalance(NumberUtils.keepPrecision(memberStatistics.getMonthBalance() -
                (memberOrder.getPriceAfterDiscount() + memberOrder.getSendPrice()), 2));
        memberStatistics.setModifiedTime(new Timestamp(System.currentTimeMillis()));
        memberStatisticsDao.save(memberStatistics);

        memberOrder.setPaywayID(payWayID);
        memberOrder.setPayTime(new Timestamp(System.currentTimeMillis()));
        memberOrder.setPayTotal(NumberUtils.keepPrecision(memberOrder.getPriceAfterDiscount() + memberOrder.getSendPrice(), 2));
        memberOrder.setStatus(memberOrder.getSendType() == 1 ? 4 : 8);
        memberOrder.setPayFrom(4);
        memberOrderDao.save(memberOrder);

        List<MemberOrderGoods> memberOrderGoodsList = memberOrderGoodsDao.getMemberOrderGoodsByMemberOrderID(memberOrder.getId());
        for (MemberOrderGoods memberOrderGoods : memberOrderGoodsList) {
            memberOrderGoods.setStatus(4);
            memberOrderGoodsDao.save(memberOrderGoods);
        }
    }

    private void editFare(MemberOrder memberOrder) {
        try {
            String orderAmountFreeFare = parameterDao.getParemeterValueByCode("orderAmountFreeFare");
            String orderFare = parameterDao.getParemeterValueByCode("orderFare");
            if (StringUtils.isNotBlank(orderAmountFreeFare)
                    && StringUtils.isNotBlank(orderFare)
                    && Double.parseDouble(orderAmountFreeFare) != 0
                    && memberOrder.getPriceAfterDiscount() < Integer.parseInt(orderAmountFreeFare)) {
//                memberOrder.setPriceTotal(NumberUtils.keepPrecision(memberOrder.getPriceTotal() + Double.parseDouble(orderFare), 2));
//                memberOrder.setPriceAfterDiscount(NumberUtils.keepPrecision(memberOrder.getPriceAfterDiscount() + Double.parseDouble(orderFare), 2));
                memberOrder.setSendPrice(Double.parseDouble(orderFare));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void delPoint(Member member, int point) {
        MemberStatistics memberStatistics = memberStatisticsDao.getByMemberId(member.getId());
        memberStatistics.setPointTotalUsed(memberStatistics.getPointTotalUsed() + point);
        memberStatistics.setPoint(memberStatistics.getPoint() - point);
        memberStatisticsDao.save(memberStatistics);

        Point pointObj = new Point();
        pointObj.setApplicationID(member.getApplicationID());
        pointObj.setMemberID(member.getId());
        pointObj.setCompanyID(member.getCompanyID());
        pointObj.setSiteID(member.getSiteID());
        pointObj.setOperateTime(new Timestamp(System.currentTimeMillis()));
        pointObj.setAction("购买商品积分抵扣");
        pointObj.setActionType(2);
        pointObj.setNumber(point);
        pointObj.setObjectDefineID(Config.MEMBERORDER_OBJECTDEFINEID);
        pointObj.setObjectID(member.getId());
        pointObj.setObjectName(member.getLoginName());
        pointObj.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        pointObj.setModifiedTime(new Timestamp(System.currentTimeMillis()));
        pointDao.save(pointObj);
    }
}
