package com.kingpivot.base.memberMajor.dao;

import com.kingpivot.base.memberMajor.model.MemberMajor;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "memberMajor")
@Qualifier("memberMajorDao")
public interface MemberMajorDao extends BaseDao<MemberMajor, String> {
    @Query(value = "SELECT id FROM memberMajor WHERE majorID=?1 AND memberID=?2 AND isValid=1 AND isLock=0 LIMIT 1",nativeQuery = true)
    String getMemberMajorIdByMajorIDAndMemberID(String majorID,String memberID);

    @Query(value = "SELECT * FROM memberMajor WHERE majorID=?1 AND memberID=?2 AND isValid=1 AND isLock=0 LIMIT 1",nativeQuery = true)
    MemberMajor getMemberMajorByMajorIdAndMemberId(String majorID, String memberID);
}
