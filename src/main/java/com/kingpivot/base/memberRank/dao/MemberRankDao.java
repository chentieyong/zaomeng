package com.kingpivot.base.memberRank.dao;

import com.kingpivot.base.memberRank.model.MemberRank;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "memberrank")
@Qualifier("memberRankDao")
public interface MemberRankDao extends BaseDao<MemberRank, String> {
    @Query(value = "SELECT tb_rank.depositeRate FROM memberrank tb_mbrank LEFT JOIN rank tb_rank ON tb_mbrank.rankID=tb_rank.id\n" +
            "     WHERE tb_mbrank.memberID=?1 AND tb_mbrank.isValid=1 AND tb_mbrank.isLock=0 ORDER BY tb_rank.orderSeq DESC LIMIT 1", nativeQuery = true)
    Double getDepositeRateByMemberId(String memberID);
}
