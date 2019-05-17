package com.kingpivot.base.memberstatistics.service;

import com.kingpivot.base.memberstatistics.model.MemberStatistics;
import com.kingpivot.common.service.BaseService;

public interface MemberStatisticsService extends BaseService<MemberStatistics, String> {
    MemberStatistics getByMemberId(String memberId);

    int getMemberPoint(String memberId);
}
