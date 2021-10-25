package com.kingpivot.base.browse.dao;

import com.kingpivot.base.browse.model.Browse;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "browse")
@Qualifier("browseDao")
public interface BrowseDao extends BaseDao<Browse, String> {
    @Query(value = "SELECT id FROM browse WHERE objectID=?1 AND memberID=?2 AND isValid=1 AND isLock=0 limit 1", nativeQuery = true)
    String getBrowseByObjectIDAndMemberID(String objectID, String memberID);

    @Query(value = "SELECT COUNT(id) FROM browse WHERE objectDefineID=?1 AND memberID=?2 AND isValid=1 AND isLock=0", nativeQuery = true)
    int getBrowseNumByObjectDefineIDAndMemberID(String objectDefineID, String memberID);
}
