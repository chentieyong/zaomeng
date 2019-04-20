package com.kingpivot.base.collect.dao;

import com.kingpivot.base.collect.model.Collect;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "collect")
@Qualifier("collectDao")
public interface CollectDao extends BaseDao<Collect, String> {
}
