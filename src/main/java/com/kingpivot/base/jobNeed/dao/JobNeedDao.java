package com.kingpivot.base.jobNeed.dao;

import com.kingpivot.base.jobNeed.model.JobNeed;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "jobNeed")
@Qualifier("jobNeedDao")
public interface JobNeedDao extends BaseDao<JobNeed, String> {
}
