package com.kingpivot.base.goodsShopCase.dao;

import com.kingpivot.base.goodsShopCase.model.GoodsShopCase;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "goodsShopCase")
@Qualifier("goodsShopCaseDao")
public interface GoodsShopCaseDao extends BaseDao<GoodsShopCase, String> {
}
