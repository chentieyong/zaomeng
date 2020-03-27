package com.kingpivot.base.memberRaffle.service.impl;

import com.kingpivot.base.memberRaffle.dao.MemberRaffleDao;
import com.kingpivot.base.memberRaffle.model.MemberRaffle;
import com.kingpivot.base.memberRaffle.service.MemberRaffleService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("memberRaffleService")
public class MemberRaffleServiceImpl extends BaseServiceImpl<MemberRaffle, String> implements MemberRaffleService {

    @Resource(name = "memberRaffleDao")
    private MemberRaffleDao memberRaffleDao;

    @Override
    public BaseDao<MemberRaffle, String> getDAO() {
        return this.memberRaffleDao;
    }
}

