package com.kingpivot.base.site.service.impl;

import com.kingpivot.base.site.dao.SiteDao;
import com.kingpivot.base.site.model.Site;
import com.kingpivot.base.site.service.SiteService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("siteService")
public class SiteServiceImpl extends BaseServiceImpl<Site, String> implements SiteService {

    @Resource(name = "siteDao")
    private SiteDao siteDao;

    @Override
    public BaseDao<Site, String> getDAO() {
        return this.siteDao;
    }
}
