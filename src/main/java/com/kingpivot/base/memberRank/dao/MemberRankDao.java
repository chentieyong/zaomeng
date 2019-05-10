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
}
