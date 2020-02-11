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

    @Query(value = "SELECT GROUP_CONCAT(tb_major.`name`) FROM memberMajor tb_mb_major LEFT JOIN major" +
            " tb_major ON tb_mb_major.majorID=tb_major.id WHERE tb_mb_major.memberID=?1 and tb_mb_major.status=2 AND tb_mb_major.isValid=1" +
            " AND tb_mb_major.isLock=0",nativeQuery = true)
    String getMajorNameByMemberId(String memberId);
}
