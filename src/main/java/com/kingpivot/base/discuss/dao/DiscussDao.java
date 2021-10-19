package com.kingpivot.base.discuss.dao;

import com.kingpivot.base.discuss.model.Discuss;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "discuss")
@Qualifier("discussDao")
public interface DiscussDao extends BaseDao<Discuss, String> {
}
