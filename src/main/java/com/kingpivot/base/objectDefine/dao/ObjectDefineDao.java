package com.kingpivot.base.objectDefine.dao;

import com.kingpivot.base.objectDefine.model.ObjectDefine;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "objectdefine")
@Qualifier("objectDefineDao")
public interface ObjectDefineDao extends BaseDao<ObjectDefine, String> {
}
