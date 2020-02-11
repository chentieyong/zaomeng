package com.kingpivot.base.memberMajor.service;

import com.kingpivot.base.memberMajor.model.MemberMajor;
import com.kingpivot.common.service.BaseService;

public interface MemberMajorService extends BaseService<MemberMajor, String> {
    boolean isApply(String majorID, String memberID);

    MemberMajor getMemberMajorByMajorIdAndMemberId(String majorID, String memberID);

    String getMajorNameByMemberId(String memberId);
}
