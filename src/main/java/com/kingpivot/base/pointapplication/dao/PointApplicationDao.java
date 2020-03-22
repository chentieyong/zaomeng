package com.kingpivot.base.pointapplication.dao;

import com.kingpivot.base.pointapplication.model.PointApplication;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "pointApplication")
@Qualifier("pointApplicationDao")
public interface PointApplicationDao extends BaseDao<PointApplication, String> {
    @Query(value = "SELECT number FROM pointApplication WHERE applicationID=?1 AND " +
            "pointDefineID=(SELECT id FROM pointDefine WHERE name=?2 AND isValid=1 AND isLock=0 LIMIT 1) LIMIT 1"
            , nativeQuery = true)
    Integer getNumberByAppIdAndPointName(String applicationID, String pointName);
}
