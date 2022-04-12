package com.kingpivot.base.memberOrder.service;

import com.kingpivot.base.cartGoods.model.CartGoods;
import com.kingpivot.base.goodsShop.model.GoodsShop;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.memberOrder.model.MemberOrder;
import com.kingpivot.base.memberstatistics.model.MemberStatistics;
import com.kingpivot.common.service.BaseService;

import java.util.List;

/**
 * Created by Administrator on 2016/9/18.
 */
public interface MemberOrderService extends BaseService<MemberOrder, String> {

    /**
     * 商品详情生成订单
     *
     * @param member
     * @param goodsShop
     * @param objectFeatureItemID1
     * @param qty
     * @param contactName
     * @param contactPhone
     * @param address
     * @param memberBonusID
     * @return
     */
    String createMemberOrder(Member member, GoodsShop goodsShop, String objectFeatureItemID1,
                             int qty, String contactName, String contactPhone,
                             String address, String memberBonusID, String orderType, String sendType);

    /**
     * 单店铺生成订单
     *
     * @param cartGoodsList
     * @param member
     * @param contactName
     * @param contactPhone
     * @param address
     * @param memberBonusID
     * @return
     */
    String createMemberOrderFromCart(List<CartGoods> cartGoodsList, Member member, String contactName, String contactPhone, String address, String memberBonusID, String sendType);

    /**
     * 多店铺生成订单
     *
     * @param shopList
     * @param cartID
     * @param member
     * @param contactName
     * @param contactPhone
     * @param address
     * @param memberBonusID
     * @return
     */
    String createMemberOrderFromShopCart(List<String> shopList, String cartID, Member member, String contactName, String contactPhone, String address, String memberBonusID);

    /**
     * 根据memberPaymentID获取订单
     *
     * @param memberPaymentID
     * @return
     */
    List<MemberOrder> getMemberOrderByMemberPayMentID(String memberPaymentID);

    /**
     * 更改会员订单memberPaymentID
     *
     * @param paywayID
     * @param memberPaymentID
     */
    void updateMemberOrderByMemberPaymentID(String paywayID, String memberPaymentID);

    void updateMemberOrderStatus(String memberOrderID, int status);

    void monthBalancePayMemberOrder(MemberOrder memberOrder, MemberStatistics memberStatistics, String payWayID);
}

