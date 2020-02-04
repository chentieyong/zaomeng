package com.kingpivot.base.capitalPost.service.impl;

import com.kingpivot.base.capitalPost.dao.CapitalPostDao;
import com.kingpivot.base.capitalPost.model.CapitalPost;
import com.kingpivot.base.capitalPost.service.CapitalPostService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("capitalPostService")
public class CapitalPostServiceImpl extends BaseServiceImpl<CapitalPost, String> implements CapitalPostService {
    @Autowired
    private CapitalPostDao capitalPostDao;

    @Override
    public BaseDao<CapitalPost, String> getDAO() {
        return this.capitalPostDao;
    }

}
