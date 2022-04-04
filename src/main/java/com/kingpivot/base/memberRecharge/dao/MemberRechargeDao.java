package com.kingpivot.base.memberRecharge.dao;

import com.kingpivot.base.memberRecharge.model.MemberRecharge;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "memberRecharge")
@Qualifier("memberRechargeDao")
public interface MemberRechargeDao extends BaseDao<MemberRecharge, String> {
}
