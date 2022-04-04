package com.kingpivot.base.memberRecharge.service.impl;

import com.kingpivot.base.memberRecharge.dao.MemberRechargeDao;
import com.kingpivot.base.memberRecharge.model.MemberRecharge;
import com.kingpivot.base.memberRecharge.service.MemberRechargeService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("memberRechargeService")
public class MemberRechargeServiceImpl extends BaseServiceImpl<MemberRecharge, String> implements MemberRechargeService {

    @Resource(name = "memberRechargeDao")
    private MemberRechargeDao memberRechargeDao;

    @Override
    public BaseDao<MemberRecharge, String> getDAO() {
        return this.memberRechargeDao;
    }
}
