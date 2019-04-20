package com.kingpivot.base.objectFeatureItem.dao;

import com.kingpivot.base.objectFeatureItem.model.ObjectFeatureItem;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "objectFeatureItem")
@Qualifier("objectFeatureItemDao")
public interface ObjectFeatureItemDao extends BaseDao<ObjectFeatureItem, String> {

}
