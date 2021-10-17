package com.kingpivot.base.objectFeatureData.dao;

import com.kingpivot.base.objectFeatureData.model.ObjectFeatureData;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "objectFeatureData")
@Qualifier("objectFeatureDataDao")
public interface ObjectFeatureDataDao extends BaseDao<ObjectFeatureData, String> {

    @Query(value = "SELECT * FROM objectFeatureData WHERE objectID=?1 and objectFeatureItemID1=?2 and isValid=1 and isLock=0 limit 1", nativeQuery = true)
    ObjectFeatureData getObjectFetureData(String objectID, String objectFeatureItemID1);
}
