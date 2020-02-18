package com.kingpivot.base.memberMajor.service.impl;

import com.kingpivot.base.major.dao.MajorDao;
import com.kingpivot.base.major.model.Major;
import com.kingpivot.base.memberMajor.dao.MemberMajorDao;
import com.kingpivot.base.memberMajor.model.MemberMajor;
import com.kingpivot.base.memberMajor.service.MemberMajorService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;

@Service("memberMajorService")
public class MemberMajorServiceImpl extends BaseServiceImpl<MemberMajor, String> implements MemberMajorService {

    @Resource(name = "memberMajorDao")
    private MemberMajorDao memberMajorDao;

    @Resource(name = "majorDao")
    private MajorDao majorDao;

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

    @Override
    public MemberMajor getMemberMajorByMajorIdAndMemberId(String majorID, String memberID) {
        return memberMajorDao.getMemberMajorByMajorIdAndMemberId(majorID, memberID);
    }

    @Override
    public String getMajorNameByMemberId(String memberId) {
        return memberMajorDao.getMajorNameByMemberId(memberId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String applyOneMajor(MemberMajor memberMajor, Major major) {
        memberMajor.setStatus(1);
        memberMajor.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        memberMajorDao.save(memberMajor);

        //更新数量
        major.setAlreadyUpgradeNumber(major.getAlreadyUpgradeNumber() + 1);
        majorDao.save(major);
        return memberMajor.getId();
    }
}
