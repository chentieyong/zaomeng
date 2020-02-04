package com.kingpivot.base.jobNeed.service.impl;

import com.kingpivot.base.jobNeed.dao.JobNeedDao;
import com.kingpivot.base.jobNeed.model.JobNeed;
import com.kingpivot.base.jobNeed.service.JobNeedService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("jobNeedService")
public class JobNeedServiceImpl extends BaseServiceImpl<JobNeed, String> implements JobNeedService {
    @Autowired
    private JobNeedDao jobNeedDao;

    @Override
    public BaseDao<JobNeed, String> getDAO() {
        return jobNeedDao;
    }
}
