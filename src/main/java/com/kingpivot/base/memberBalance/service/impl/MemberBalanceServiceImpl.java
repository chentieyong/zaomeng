package com.kingpivot.base.memberBalance.service.impl;

import com.kingpivot.base.memberBalance.dao.MemberBalanceDao;
import com.kingpivot.base.memberBalance.model.MemberBalance;
import com.kingpivot.base.memberBalance.service.MemberBalanceService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("memberBalanceService")
public class MemberBalanceServiceImpl extends BaseServiceImpl<MemberBalance, String> implements MemberBalanceService {

    @Resource(name = "memberBalanceDao")
    private MemberBalanceDao memberBalanceDao;

    @Override
    public BaseDao<MemberBalance, String> getDAO() {
        return this.memberBalanceDao;
    }
}
