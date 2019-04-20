package com.kingpivot.base.memberOrder.dao;

import com.kingpivot.base.memberOrder.model.MemberOrder;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "memberOrder")
@Qualifier("memberOrderDao")
public interface MemberOrderDao extends BaseDao<MemberOrder, String> {
}
