package com.kingpivot.base.objectFeatureData.dao;

import com.kingpivot.base.objectFeatureData.model.ObjectFeatureData;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "objectFeatureData")
@Qualifier("objectFeatureDataDao")
public interface ObjectFeatureDataDao extends BaseDao<ObjectFeatureData, String> {
}
