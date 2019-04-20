package com.kingpivot.base.rank.dao;

import com.kingpivot.base.rank.model.Rank;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "rank")
@Qualifier("rankDao")
public interface RankDao extends BaseDao<Rank, String> {

}
