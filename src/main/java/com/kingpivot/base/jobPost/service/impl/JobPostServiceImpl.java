package com.kingpivot.base.jobPost.service.impl;

import com.kingpivot.base.jobPost.dao.JobPostDao;
import com.kingpivot.base.jobPost.model.JobPost;
import com.kingpivot.base.jobPost.service.JobPostService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("jobPostService")
public class JobPostServiceImpl extends BaseServiceImpl<JobPost, String> implements JobPostService {

    @Resource(name = "jobPostDao")
    private JobPostDao jobPostDao;

    @Override
    public BaseDao<JobPost, String> getDAO() {
        return this.jobPostDao;
    }
}

