package com.kingpivot.base.memberInvoiceDefine.dao;

import com.kingpivot.base.memberInvoiceDefine.model.MemberInvoiceDefine;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Table;

@Repository
@Table(name = "memberInvoiceDefine")
@Qualifier("memberInvoiceDefineDao")
public interface MemberInvoiceDefineDao extends BaseDao<MemberInvoiceDefine, String> {

    @Transactional
    @Query(value = "UPDATE memberInvoiceDefine SET isDefault=0 WHERE memberID=?1 AND if(?2 is not null,id!=?2,2=2)", nativeQuery = true)
    @Modifying
    void updateMemberInvoiceDefineDefault(String memberID, String notEqMemberInvoiceDefineID);

    @Query(value = "SELECT MAX(orderSeq) FROM memberInvoiceDefine WHERE memberID=?1 AND isValid=1 AND isLock=0", nativeQuery = true)
    Integer getMaxOrderSeq(String memberID);
}
