package com.kingpivot.base.memberOrder.service;

import com.kingpivot.base.cartGoods.model.CartGoods;
import com.kingpivot.base.goodsShop.model.GoodsShop;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.memberOrder.model.MemberOrder;
import com.kingpivot.common.service.BaseService;

import java.util.List;

/**
 * Created by Administrator on 2016/9/18.
 */
public interface MemberOrderService extends BaseService<MemberOrder, String> {

    String createMemberOrder(Member member, GoodsShop goodsShop, String objectFeatureItemID1, int qty, String contactName, String contactPhone, String address,String memberBonusID);

    String createMemberOrderFromCart(List<CartGoods> cartGoodsList, Member member, String contactName, String contactPhone, String address,String memberBonusID);

    List<MemberOrder> getMemberOrderByMemberPayMentID(String memberPaymentID);
}

