package com.kingpivot.base.memberSearch.dao;

import com.kingpivot.base.memberSearch.model.MemberSearch;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Table;

@Repository
@Table(name = "memberSearch")
@Qualifier("memberSearchDao")
public interface MemberSearchDao extends BaseDao<MemberSearch, String> {

    @Transactional
    @Query(value = "UPDATE memberSearch SET isValid=0 WHERE memberID=?1",nativeQuery = true)
    @Modifying
    void deleteMemberSearch(String memberID);
}
