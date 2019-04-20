package com.kingpivot.base.memberSearch.dao;

import com.kingpivot.base.memberSearch.model.MemberSearch;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "memberSearch")
@Qualifier("memberSearchDao")
public interface MemberSearchDao extends BaseDao<MemberSearch, String> {
}
