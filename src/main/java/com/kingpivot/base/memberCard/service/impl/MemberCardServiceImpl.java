package com.kingpivot.base.memberCard.service.impl;

import com.kingpivot.base.memberCard.dao.MemberCardDao;
import com.kingpivot.base.memberCard.model.MemberCard;
import com.kingpivot.base.memberCard.service.MemberCardService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("memberCardService")
public class MemberCardServiceImpl extends BaseServiceImpl<MemberCard, String> implements MemberCardService {

    @Resource(name = "memberCardDao")
    private MemberCardDao memberCardDao;

    @Override
    public BaseDao<MemberCard, String> getDAO() {
        return this.memberCardDao;
    }

    @Override
    public MemberCard getEffectiveMemberCard(String memberID) {
        return memberCardDao.getEffectiveMemberCard(memberID);
    }
}
