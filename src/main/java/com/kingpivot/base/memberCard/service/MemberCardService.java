package com.kingpivot.base.memberCard.service;

import com.kingpivot.base.memberCard.model.MemberCard;
import com.kingpivot.common.service.BaseService;

public interface MemberCardService extends BaseService<MemberCard, String> {
    MemberCard getEffectiveMemberCard(String memberID);

    int getCountEffectiveMemberCard(String memberID);

    int getCountEffectiveMemberCardByCardDefineID(String memberID, String cardDefineID);
}
