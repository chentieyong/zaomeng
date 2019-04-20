package com.kingpivot.base.featureDefine.dao;

import com.kingpivot.base.featureDefine.model.FeatureDefine;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "featureDefine")
@Qualifier("featureDefineDao")
public interface FeatureDefineDao extends BaseDao<FeatureDefine, String> {

}
