package com.kingpivot.base.helpNeed.dao;

import com.kingpivot.base.helpNeed.model.HelpNeed;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "helpNeed")
@Qualifier("helpNeedDao")
public interface HelpNeedDao extends BaseDao<HelpNeed, String> {
}
