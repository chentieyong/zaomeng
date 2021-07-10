package com.kingpivot.base.partyMember.dao;

import com.kingpivot.base.partyMember.model.PartyMember;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "partyMember")
@Qualifier("partyMemberDao")
public interface PartyMemberDao extends BaseDao<PartyMember, String> {

    @Query(value = "select * from partyMember where name=?1 and isValid=1 and isLock=0", nativeQuery = true)
    PartyMember findByName(String name);
}
