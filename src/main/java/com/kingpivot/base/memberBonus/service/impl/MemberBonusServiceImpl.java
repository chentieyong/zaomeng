package com.kingpivot.base.memberBonus.service.impl;

import com.kingpivot.base.memberBonus.dao.MemberBonusDao;
import com.kingpivot.base.memberBonus.model.MemberBonus;
import com.kingpivot.base.memberBonus.service.MemberBonusService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("memberBonusService")
public class MemberBonusServiceImpl extends BaseServiceImpl<MemberBonus, String> implements MemberBonusService {

	@Resource(name = "memberBonusDao")
	private MemberBonusDao memberBonusDao;

	@Override
	public BaseDao<MemberBonus, String> getDAO() {
		return this.memberBonusDao;
	}

	@Override
	public void initMemberBonusByMemberOrderID(String memberOrderID) {
		memberBonusDao.initMemberBonusByMemberOrderID(memberOrderID);
	}
}


