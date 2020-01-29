package com.kingpivot.base.praise.dao;

import com.kingpivot.base.praise.model.Praise;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "praise")
@Qualifier("praiseDao")
public interface PraiseDao extends BaseDao<Praise, String> {
}





