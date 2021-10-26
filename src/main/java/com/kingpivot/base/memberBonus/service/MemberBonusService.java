package com.kingpivot.base.memberBonus.service;

import com.kingpivot.base.memberBonus.model.MemberBonus;
import com.kingpivot.common.service.BaseService;

public interface MemberBonusService extends BaseService<MemberBonus, String> {

    void initMemberBonusByMemberOrderID(String memberOrderID);

    int getMemberBonusNum(int status, String memberID);

    int getMyBonusByBonusId(String memberID, String bonusID);
}
