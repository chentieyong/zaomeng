package com.kingpivot.base.release.service.impl;

import com.kingpivot.base.release.dao.ReleaseDao;
import com.kingpivot.base.release.model.Release;
import com.kingpivot.base.release.service.ReleaseService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("releaseService")
public class ReleaseServiceImpl extends BaseServiceImpl<Release, String> implements ReleaseService {

    @Resource(name = "releaseDao")
    private ReleaseDao releaseDao;

    @Override
    public BaseDao<Release, String> getDAO() {
        return this.releaseDao;
    }
}
