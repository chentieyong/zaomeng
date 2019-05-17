package com.kingpivot.base.memberstatistics.service.impl;

import com.kingpivot.base.memberstatistics.dao.MemberStatisticsDao;
import com.kingpivot.base.memberstatistics.model.MemberStatistics;
import com.kingpivot.base.memberstatistics.service.MemberStatisticsService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("memberStatisticsService")
public class MemberStatisticsServiceImpl extends BaseServiceImpl<MemberStatistics, String> implements MemberStatisticsService {

    @Resource(name = "memberStatisticsDao")
    private MemberStatisticsDao memberStatisticsDao;

    @Override
    public BaseDao<MemberStatistics, String> getDAO() {
        return this.memberStatisticsDao;
    }

    @Override
    public MemberStatistics getByMemberId(String memberId) {
        return memberStatisticsDao.getByMemberId(memberId);
    }

    @Override
    public int getMemberPoint(String memberId) {
        Integer val = memberStatisticsDao.getMemberPoint(memberId);
        if (val == null) {
            return 0;
        }
        return val.intValue();
    }
}
