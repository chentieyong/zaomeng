package com.kingpivot.base.memberTemp.service.impl;

import com.kingpivot.base.memberTemp.dao.MemberTempDao;
import com.kingpivot.base.memberTemp.model.MemberTemp;
import com.kingpivot.base.memberTemp.service.MemberTempService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("memberTempService")
public class MemberServiceImpl extends BaseServiceImpl<MemberTemp, String> implements MemberTempService {
    @Autowired
    private MemberTempDao memberTempDao;

    @Override
    public BaseDao<MemberTemp, String> getDAO() {
        return this.memberTempDao;
    }

    @Override
    public List<MemberTemp> getList() {
        return memberTempDao.getList();
    }
}
