package com.kingpivot.base.member.service;

import com.kingpivot.base.member.model.Member;
import com.kingpivot.common.service.BaseService;

public interface MemberService extends BaseService<Member, String> {

    Member getMemberByLoginNameAndApplicationId(String loginName, String applicationID);

    String getCurRecommandCode(String applicationId);

    String getMemberIdByPhoneAndApplicationId(String phone, String applicationID);

    Member getMemberByPhoneAndApplicationId(String phone, String applicationId);

    String getMemberApplicationID(String memberID);

    Member getMemberByWeixinCodeAndAppId(String code, String applicationID);

    String getNameById(String id);
}
