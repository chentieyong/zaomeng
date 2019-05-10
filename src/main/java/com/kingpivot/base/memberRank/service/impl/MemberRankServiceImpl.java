package com.kingpivot.base.memberRank.service.impl;

import com.kingpivot.base.memberRank.dao.MemberRankDao;
import com.kingpivot.base.memberRank.model.MemberRank;
import com.kingpivot.base.memberRank.service.MemberRankService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("memberRankService")
public class MemberRankServiceImpl extends BaseServiceImpl<MemberRank, String> implements MemberRankService {

    @Resource(name = "memberRankDao")
    private MemberRankDao memberRankDao;

    @Override
    public BaseDao<MemberRank, String> getDAO() {
        return this.memberRankDao;
    }
}
