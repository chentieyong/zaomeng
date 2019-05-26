package com.kingpivot.base.memberOrderReturnGoods.service;


import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.memberOrderGoods.model.MemberOrderGoods;
import com.kingpivot.base.memberOrderReturnGoods.model.MemberOrderGoodsReturn;
import com.kingpivot.common.service.BaseService;

public interface MemberOrderGoodsReturnService extends BaseService<MemberOrderGoodsReturn, String> {
    void memberOrderGoodsReturn(MemberOrderGoods memberOrderGoods, Member member,String description);
}
