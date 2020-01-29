package com.kingpivot.base.praise.dao;

import com.kingpivot.base.praise.model.Praise;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "praise")
@Qualifier("praiseDao")
public interface PraiseDao extends BaseDao<Praise, String> {

    @Query(value = "SELECT id FROM praise WHERE objectID=?1 AND memberID=?2 AND isValid=1 AND isLock=0 limit 1",nativeQuery = true)
    String getPraiseByObjectIDAndMemberID(String objectID, String memberID);
}





