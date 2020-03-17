package com.kingpivot.base.memberAddress.dao;

import com.kingpivot.base.memberAddress.model.MemberAddress;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Table;

@Repository
@Table(name = "memberAddress")
@Qualifier("memberAddressDao")
public interface MemberAddressDao extends BaseDao<MemberAddress, String> {

    @Transactional
    @Query(value = "UPDATE memberAddress SET isDefault=0 WHERE memberID=?1 AND if(?2 is not null,id!=?2,2=2)", nativeQuery = true)
    @Modifying
    void updateMemberAddressDefault(String memberID, String notEqMemberAddressID);
}