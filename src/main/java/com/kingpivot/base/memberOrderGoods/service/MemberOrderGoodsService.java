package com.kingpivot.base.memberOrderGoods.service;


import com.kingpivot.base.memberOrderGoods.model.MemberOrderGoods;
import com.kingpivot.common.service.BaseService;

import java.util.List;


/**
 * Created by Administrator on 2016/9/18.
 */
public interface MemberOrderGoodsService extends BaseService<MemberOrderGoods, String> {
    List<MemberOrderGoods> getMemberOrderGoodsByMemberOrderID(String memberOrderID);
}
