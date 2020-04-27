package com.kingpivot.base.memberBalance.dao;

import com.kingpivot.base.memberBalance.model.MemberBalance;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "memberBalance")
@Qualifier("memberBalanceDao")
public interface MemberBalanceDao extends BaseDao<MemberBalance, String> {
}
