package com.kingpivot.base.praise.service.impl;

import com.kingpivot.base.praise.dao.PraiseDao;
import com.kingpivot.base.praise.model.Praise;
import com.kingpivot.base.praise.service.PraiseService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("praiseService")
public class PraiseServiceImpl extends BaseServiceImpl<Praise, String> implements PraiseService {

    @Resource(name = "praiseDao")
    private PraiseDao praiseDao;

    @Override
    public BaseDao<Praise, String> getDAO() {
        return this.praiseDao;
    }

    @Override
    public String getPraiseByObjectIDAndMemberID(String objectID, String memberID) {
        return praiseDao.getPraiseByObjectIDAndMemberID(objectID, memberID);
    }
}
