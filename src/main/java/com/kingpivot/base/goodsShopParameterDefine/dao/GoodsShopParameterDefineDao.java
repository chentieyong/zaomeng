package com.kingpivot.base.goodsShopParameterDefine.dao;

import com.kingpivot.base.goodsShopParameterDefine.model.GoodsShopParameterDefine;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "goodsShopParameterDefine")
@Qualifier("goodsShopParameterDefineDao")
public interface GoodsShopParameterDefineDao extends BaseDao<GoodsShopParameterDefine, String> {
}
