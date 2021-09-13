package com.kingpivot.base.blessingTreeDetail.dao;

import com.kingpivot.base.blessingTreeDetail.model.BlessingTreeDetail;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "blessingTreeDetail")
@Qualifier("blessingTreeDetailDao")
public interface BlessingTreeDetailDao extends BaseDao<BlessingTreeDetail, String> {

}