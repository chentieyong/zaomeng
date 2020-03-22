package com.kingpivot.base.pointdefine.dao;

import com.kingpivot.base.pointdefine.model.PointDefine;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "pointDefine")
@Qualifier("pointDefineDao")
public interface PointDefineDao extends BaseDao<PointDefine, String> {
}
