package com.kingpivot.base.goodsShop.dao;

import com.kingpivot.base.goodsShop.model.GoodsShop;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "goodsShop")
@Qualifier("goodsShopDao")
public interface GoodsShopDao extends BaseDao<GoodsShop, String> {
}
