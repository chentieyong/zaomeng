package com.kingpivot.base.memberRankTemp.service;

import com.kingpivot.base.memberRankTemp.model.MemberRankTemp;
import com.kingpivot.common.service.BaseService;

public interface MemberRankTempService extends BaseService<MemberRankTemp, String> {
    MemberRankTemp findByMemberID(String memberID);
}
