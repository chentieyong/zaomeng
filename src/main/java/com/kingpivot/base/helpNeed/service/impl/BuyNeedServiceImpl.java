package com.kingpivot.base.helpNeed.service.impl;

import com.kingpivot.base.helpNeed.dao.HelpNeedDao;
import com.kingpivot.base.helpNeed.model.HelpNeed;
import com.kingpivot.base.helpNeed.service.HelpNeedService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("helpNeedService")
public class BuyNeedServiceImpl extends BaseServiceImpl<HelpNeed, String> implements HelpNeedService {
    @Autowired
    private HelpNeedDao helpNeedDao;

    @Override
    public BaseDao<HelpNeed, String> getDAO() {
        return this.helpNeedDao;
    }
}
