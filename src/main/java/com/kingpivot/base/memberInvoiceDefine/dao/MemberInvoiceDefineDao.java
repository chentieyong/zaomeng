package com.kingpivot.base.memberInvoiceDefine.dao;

import com.kingpivot.base.memberInvoiceDefine.model.MemberInvoiceDefine;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "memberInvoiceDefine")
@Qualifier("memberInvoiceDefineDao")
public interface MemberInvoiceDefineDao extends BaseDao<MemberInvoiceDefine, String> {
}
