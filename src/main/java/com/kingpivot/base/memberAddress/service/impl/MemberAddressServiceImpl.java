package com.kingpivot.base.memberAddress.service.impl;

import com.kingpivot.base.memberAddress.dao.MemberAddressDao;
import com.kingpivot.base.memberAddress.model.MemberAddress;
import com.kingpivot.base.memberAddress.service.MemberAddressService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("memberAddressService")
public class MemberAddressServiceImpl extends BaseServiceImpl<MemberAddress, String> implements MemberAddressService {
    @Resource(name = "memberAddressDao")
    private MemberAddressDao memberAddressDao;

    @Override
    public BaseDao<MemberAddress, String> getDAO() {
        return this.memberAddressDao;
    }

    @Override
    public void updateMemberAddressDefault(String memberID, String notEqMemberAddressID) {
        memberAddressDao.updateMemberAddressDefault(memberID, notEqMemberAddressID);
    }

    @Override
    public int getMaxOrderSeq(String memberID) {
        Integer val = memberAddressDao.getMaxOrderSeq(memberID);
        if (val == null) {
            return 1;
        }
        return val.intValue() + 1;
    }
}

