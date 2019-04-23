package com.kingpivot.base.memberPayment.dao;

import com.kingpivot.base.memberPayment.model.MemberPayment;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "memberPayment")
@Qualifier("memberPaymentDao")
public interface MemberPaymentDao extends BaseDao<MemberPayment,String > {

}
