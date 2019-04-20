package com.kingpivot.base.point.dao;

import com.kingpivot.base.point.model.Point;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "point")
@Qualifier("pointDao")
public interface PointDao extends BaseDao<Point, String> {
}
