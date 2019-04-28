package com.kingpivot.base.smsWay.dao;

import com.kingpivot.base.smsWay.model.SmsWay;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "smsWay")
@Qualifier("smsWayDao")
public interface SmsWayDao extends BaseDao<SmsWay, String> {
}
