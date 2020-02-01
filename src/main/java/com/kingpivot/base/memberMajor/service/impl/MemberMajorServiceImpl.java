package com.kingpivot.base.memberMajor.service.impl;

import com.kingpivot.base.memberMajor.dao.MemberMajorDao;
import com.kingpivot.base.memberMajor.model.MemberMajor;
import com.kingpivot.base.memberMajor.service.MemberMajorService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("memberMajorService")
public class MemberMajorServiceImpl extends BaseServiceImpl<MemberMajor, String> implements MemberMajorService {

    @Resource(name = "memberMajorDao")
    private MemberMajorDao memberMajorDao;

    @Override
    public BaseDao<MemberMajor, String> getDAO() {
        return this.memberMajorDao;
    }

    @Override
    public boolean isApply(String majorID, String memberID) {
        String val = memberMajorDao.getMemberMajorIdByMajorIDAndMemberID(majorID, memberID);
        if (StringUtils.isNotBlank(val)) {
            return true;
        }
        return false;
    }
}
