package com.kingpivot.base.browse.service.impl;

import com.kingpivot.base.browse.dao.BrowseDao;
import com.kingpivot.base.browse.model.Browse;
import com.kingpivot.base.browse.service.BrowseService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("browseService")
public class BrowseServiceImpl extends BaseServiceImpl<Browse, String> implements BrowseService {

    @Resource(name = "browseDao")
    private BrowseDao browseDao;

    @Override
    public BaseDao<Browse, String> getDAO() {
        return this.browseDao;
    }

    @Override
    public String getBrowseByObjectIDAndMemberID(String objectID, String memberID) {
        return browseDao.getBrowseByObjectIDAndMemberID(objectID, memberID);
    }

    @Override
    public int getBrowseNumByObjectDefineIDAndMemberID(String objectDefineID, String memberID) {
        return browseDao.getBrowseNumByObjectDefineIDAndMemberID(objectDefineID, memberID);
    }
}
