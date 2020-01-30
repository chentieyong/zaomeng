package com.kingpivot.base.member.service.impl;

import com.kingpivot.base.member.dao.MemberDao;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.member.service.MemberService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("memberService")
public class MemberServiceImpl extends BaseServiceImpl<Member, String> implements MemberService {
    @Autowired
    private MemberDao memberDao;

    @Override
    public BaseDao<Member, String> getDAO() {
        return this.memberDao;
    }

    @Override
    public Member getMemberByLoginNameAndApplicationId(String loginName, String applicationID) {
        return memberDao.getMemberByLoginNameAndApplicationId(loginName, applicationID);
    }

    @Override
    public String getCurRecommandCode(String applicationId) {
        return memberDao.getCurRecommandCode(applicationId);
    }

    @Override
    public String getMemberIdByPhoneAndApplicationId(String phone, String applicationID) {
        return memberDao.getMemberIdByPhoneAndApplicationId(phone, applicationID);
    }

    @Override
    public Member getMemberByPhoneAndApplicationId(String phone, String applicationId) {
        return memberDao.getMemberByPhoneAndApplicationId(phone, applicationId);
    }

    @Override
    public String getMemberApplicationID(String memberID) {
        return memberDao.getMemberApplicationID(memberID);
    }

    @Override
    public Member getMemberByWeixinCodeAndAppId(String code, String applicationID) {
        return memberDao.getMemberByWeixinCodeAndAppId(code, applicationID);
    }
}
