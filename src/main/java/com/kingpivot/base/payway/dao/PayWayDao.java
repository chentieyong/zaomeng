package com.kingpivot.base.payway.dao;

import com.kingpivot.base.payway.model.PayWay;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "payway")
@Qualifier("payWayDao")
public interface PayWayDao extends BaseDao<PayWay, String> {
}
