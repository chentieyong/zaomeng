package com.kingpivot.base.cardDefine.dao;

import com.kingpivot.base.cardDefine.model.CardDefine;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "cardDefine")
@Qualifier("cardDefineDao")
public interface CardDefineDao extends BaseDao<CardDefine, String> {
}
