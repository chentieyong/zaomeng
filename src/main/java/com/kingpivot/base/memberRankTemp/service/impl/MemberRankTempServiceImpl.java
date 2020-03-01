package com.kingpivot.base.memberRankTemp.service.impl;

import com.kingpivot.base.memberRankTemp.dao.MemberRankTempDao;
import com.kingpivot.base.memberRankTemp.model.MemberRankTemp;
import com.kingpivot.base.memberRankTemp.service.MemberRankTempService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("memberRankTempService")
public class MemberRankTempServiceImpl extends BaseServiceImpl<MemberRankTemp, String> implements MemberRankTempService {

    @Resource(name = "memberRankTempDao")
    private MemberRankTempDao memberRankTempDao;

    @Override
    public BaseDao<MemberRankTemp, String> getDAO() {
        return this.memberRankTempDao;
    }

    @Override
    public MemberRankTemp findByMemberID(String memberID) {
        return memberRankTempDao.findByMemberID(memberID);
    }
}
