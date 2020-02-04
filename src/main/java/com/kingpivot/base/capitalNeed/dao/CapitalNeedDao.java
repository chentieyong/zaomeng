package com.kingpivot.base.capitalNeed.dao;

import com.kingpivot.base.capitalNeed.model.CapitalNeed;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "capitalNeed")
@Qualifier("capitalNeedDao")
public interface CapitalNeedDao extends BaseDao<CapitalNeed, String> {

}
