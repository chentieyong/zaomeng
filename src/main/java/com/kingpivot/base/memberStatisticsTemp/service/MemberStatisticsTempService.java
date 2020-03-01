package com.kingpivot.base.memberStatisticsTemp.service;

import com.kingpivot.base.memberStatisticsTemp.model.MemberStatisticsTemp;
import com.kingpivot.common.service.BaseService;

public interface MemberStatisticsTempService extends BaseService<MemberStatisticsTemp, String> {
    MemberStatisticsTemp getByMemberId(String memberId);
}
