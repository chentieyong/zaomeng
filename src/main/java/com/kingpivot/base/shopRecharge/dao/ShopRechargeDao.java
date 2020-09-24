package com.kingpivot.base.shopRecharge.dao;

import com.kingpivot.base.shopRecharge.model.ShopRecharge;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "shopRecharge")
@Qualifier("shopRechargeDao")
public interface ShopRechargeDao extends BaseDao<ShopRecharge, String> {
}
