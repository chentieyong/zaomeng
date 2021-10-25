package com.kingpivot.base.collect.service.impl;

import com.kingpivot.base.collect.dao.CollectDao;
import com.kingpivot.base.collect.model.Collect;
import com.kingpivot.base.collect.service.CollectService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("collectService")
public class CollectServiceImpl extends BaseServiceImpl<Collect, String> implements CollectService {

    @Resource(name = "collectDao")
    private CollectDao collectDao;

    @Override
    public BaseDao<Collect, String> getDAO() {
        return this.collectDao;
    }

    @Override
    public String getCollectByObjectIDAndMemberID(String objectID, String memberID) {
        return collectDao.getCollectByObjectIDAndMemberID(objectID, memberID);
    }

    @Override
    public int getCollectNumByObjectDefineIDAndMemberID(String objectDefineID, String memberID) {
        return collectDao.getCollectNumByObjectDefineIDAndMemberID(objectDefineID, memberID);
    }
}
