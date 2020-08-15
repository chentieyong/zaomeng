package com.kingpivot.base.goodsShopParameter.dao;

import com.kingpivot.base.goodsShopParameter.model.GoodsShopParameter;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "goodsShopParameter")
@Qualifier("goodsShopParameterDao")
public interface GoodsShopParameterDao extends BaseDao<GoodsShopParameter, String> {
}
