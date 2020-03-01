package com.kingpivot.base.memberStatisticsTemp.service.impl;

import com.kingpivot.base.memberStatisticsTemp.dao.MemberStatisticsTempDao;
import com.kingpivot.base.memberStatisticsTemp.model.MemberStatisticsTemp;
import com.kingpivot.base.memberStatisticsTemp.service.MemberStatisticsTempService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("memberStatisticsTempService")
public class MemberStatisticsTempServiceImpl extends BaseServiceImpl<MemberStatisticsTemp, String> implements MemberStatisticsTempService {

    @Resource(name = "memberStatisticsTempDao")
    private MemberStatisticsTempDao memberStatisticsTempDao;

    @Override
    public BaseDao<MemberStatisticsTemp, String> getDAO() {
        return this.memberStatisticsTempDao;
    }

    @Override
    public MemberStatisticsTemp getByMemberId(String memberId) {
        return memberStatisticsTempDao.getByMemberId(memberId);
    }
}
