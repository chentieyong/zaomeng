package com.kingpivot.base.collect.dao;

import com.kingpivot.base.collect.model.Collect;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "collect")
@Qualifier("collectDao")
public interface CollectDao extends BaseDao<Collect, String> {
    @Query(value = "SELECT id FROM collect WHERE objectID=?1 AND memberID=?2 AND isValid=1 AND isLock=0 limit 1", nativeQuery = true)
    String getCollectByObjectIDAndMemberID(String objectID, String memberID);

    @Query(value = "SELECT COUNT(id) FROM collect WHERE objectDefineID=?1 AND memberID=?2 AND isValid=1 AND isLock=0", nativeQuery = true)
    int getCollectNumByObjectDefineIDAndMemberID(String objectDefineID, String memberID);
}
