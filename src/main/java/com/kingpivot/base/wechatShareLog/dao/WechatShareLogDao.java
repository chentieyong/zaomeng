package com.kingpivot.base.wechatShareLog.dao;

import com.kingpivot.base.wechatShareLog.model.WechatShareLog;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "wechatShareLog")
@Qualifier("wechatShareLogDao")
public interface WechatShareLogDao extends BaseDao<WechatShareLog, String> {

}
