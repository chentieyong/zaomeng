package com.kingpivot.base.siteView.service.impl;

import com.kingpivot.base.siteView.dao.SiteViewDao;
import com.kingpivot.base.siteView.model.SiteView;
import com.kingpivot.base.siteView.service.SiteViewService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("siteViewService")
public class SiteViewServiceImpl extends BaseServiceImpl<SiteView, String> implements SiteViewService {

    @Resource(name = "siteViewDao")
    private SiteViewDao siteViewDao;

    @Override
    public BaseDao<SiteView, String> getDAO() {
        return this.siteViewDao;
    }

}
