package com.kingpivot.base.memberOrderGoods.service.impl;

import com.kingpivot.base.memberOrderGoods.dao.MemberOrderGoodsDao;
import com.kingpivot.base.memberOrderGoods.model.MemberOrderGoods;
import com.kingpivot.base.memberOrderGoods.service.MemberOrderGoodsService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2016/9/18.
 */
@Service("memberOrderGoodsService")
public class MemberOrderGoodsServiceImpl extends BaseServiceImpl<MemberOrderGoods, String> implements MemberOrderGoodsService {
    @Resource(name = "memberOrderGoodsDao")
    private MemberOrderGoodsDao memberOrderGoodsDao;

    @Override
    public BaseDao<MemberOrderGoods, String> getDAO() {
        return this.memberOrderGoodsDao;
    }

    @Override
    public List<MemberOrderGoods> getMemberOrderGoodsByMemberOrderID(String memberOrderID) {
        return memberOrderGoodsDao.getMemberOrderGoodsByMemberOrderID(memberOrderID);
    }
}
