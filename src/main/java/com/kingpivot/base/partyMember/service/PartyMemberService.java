package com.kingpivot.base.partyMember.service;

import com.kingpivot.base.partyMember.model.PartyMember;
import com.kingpivot.common.service.BaseService;

public interface PartyMemberService extends BaseService<PartyMember, String> {
    PartyMember findByName(String name);
}
