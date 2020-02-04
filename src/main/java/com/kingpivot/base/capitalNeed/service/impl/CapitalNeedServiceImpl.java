package com.kingpivot.base.capitalNeed.service.impl;

import com.kingpivot.base.capitalNeed.dao.CapitalNeedDao;
import com.kingpivot.base.capitalNeed.model.CapitalNeed;
import com.kingpivot.base.capitalNeed.service.CapitalNeedService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("capitalNeedService")
public class CapitalNeedServiceImpl extends BaseServiceImpl<CapitalNeed, String> implements CapitalNeedService {

    @Autowired
    private CapitalNeedDao capitalNeedDao;

    @Override
    public BaseDao<CapitalNeed, String> getDAO() {
        return this.capitalNeedDao;
    }
}
