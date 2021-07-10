package com.kingpivot.base.partyMember.service.impl;

import com.kingpivot.base.partyMember.dao.PartyMemberDao;
import com.kingpivot.base.partyMember.model.PartyMember;
import com.kingpivot.base.partyMember.service.PartyMemberService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("partyMemberService")
public class PartyMemberServiceImpl extends BaseServiceImpl<PartyMember, String> implements PartyMemberService {

    @Resource(name = "partyMemberDao")
    private PartyMemberDao partyMemberDao;

    @Override
    public BaseDao<PartyMember, String> getDAO() {
        return this.partyMemberDao;
    }

    @Override
    public PartyMember findByName(String name) {
        return partyMemberDao.findByName(name);
    }
}
