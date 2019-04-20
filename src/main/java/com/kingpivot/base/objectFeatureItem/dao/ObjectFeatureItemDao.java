package com.kingpivot.base.objectFeatureItem.dao;

import com.kingpivot.base.objectFeatureItem.model.ObjectFeatureItem;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "objectFeatureItem")
@Qualifier("objectFeatureItemDao")
public interface ObjectFeatureItemDao extends BaseDao<ObjectFeatureItem, String> {

    @Query(value = "SELECT featureDefineID,featureDefineName FROM objectFeatureItem WHERE objectID=?1 " +
            "AND isValid=1 AND isLock=0 GROUP BY featureDefineID ORDER BY featireDefineOrderSeq", nativeQuery = true)
    Object[] getFeatureItemDefineList(String objectID);

    @Query(value = "SELECT name,id,listImage,faceImage FROM objectFeatureItem WHERE objectID=?1 AND isValid=1 AND isLock=0 AND featureDefineID=?2", nativeQuery = true)
    Object[] getFeatureItemList(String objectID, String featureDefineID);
}
