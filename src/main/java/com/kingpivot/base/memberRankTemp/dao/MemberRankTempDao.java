package com.kingpivot.base.memberRankTemp.dao;

import com.kingpivot.base.memberRankTemp.model.MemberRankTemp;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "memberRank_temp")
@Qualifier("memberRankTempDao")
public interface MemberRankTempDao extends BaseDao<MemberRankTemp, String> {
    @Query(value = "SELECT * FROM memberRank_temp WHERE memberID=?1 LIMIT 1", nativeQuery = true)
    MemberRankTemp findByMemberID(String memberID);
}
