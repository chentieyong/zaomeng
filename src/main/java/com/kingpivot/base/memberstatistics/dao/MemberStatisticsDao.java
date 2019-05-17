package com.kingpivot.base.memberstatistics.dao;

import com.kingpivot.base.memberstatistics.model.MemberStatistics;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "memberstatistics")
@Qualifier("memberStatisticsDao")
public interface MemberStatisticsDao extends BaseDao<MemberStatistics, String> {
    @Query(value = "SELECT * FROM memberstatistics WHERE memberID=?1 AND isValid=1 limit 1", nativeQuery = true)
    MemberStatistics getByMemberId(String memberId);

    @Query(value = "select point from memberstatistics where memberID=?1 AND isValid=1 limit 1", nativeQuery = true)
    Integer getMemberPoint(String memberId);
}





