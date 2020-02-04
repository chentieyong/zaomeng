package com.kingpivot.base.jobPost.dao;

import com.kingpivot.base.jobPost.model.JobPost;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "jobPost")
@Qualifier("jobPostDao")
public interface JobPostDao extends BaseDao<JobPost, String> {

}