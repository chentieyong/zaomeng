package com.kingpivot.base.memberStatisticsTemp.dao;

import com.kingpivot.base.memberStatisticsTemp.model.MemberStatisticsTemp;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "memberStatistics_temp")
@Qualifier("memberStatisticsTempDao")
public interface MemberStatisticsTempDao extends BaseDao<MemberStatisticsTemp, String> {
    @Query(value = "SELECT * FROM memberStatistics_temp WHERE memberID=?1 AND isValid=1 limit 1", nativeQuery = true)
    MemberStatisticsTemp getByMemberId(String memberId);
}





