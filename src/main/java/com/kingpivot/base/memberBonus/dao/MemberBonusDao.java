package com.kingpivot.base.memberBonus.dao;

import com.kingpivot.base.memberBonus.model.MemberBonus;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Table;

@Repository
@Table(name = "memberbonus")
@Qualifier("memberBonusDao")
public interface MemberBonusDao extends BaseDao<MemberBonus, String> {

    @Transactional
    @Query(value = "UPDATE memberbonus SET memberOrderID=NULL,useTime=NULL WHERE memberOrderID=?1 AND isValid=1 AND isLock=0", nativeQuery = true)
    @Modifying
    void initMemberBonusByMemberOrderID(String memberOrderID);

    @Query(value = "SELECT COUNT(id) FROM memberbonus WHERE `status`=?1 AND memberID=?2 AND isValid=1 AND isLock=0", nativeQuery = true)
    int getMemberBonusNum(int status, String memberID);
}
