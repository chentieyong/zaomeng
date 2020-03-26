package com.kingpivot.base.goodsChange.dao;

import com.kingpivot.base.goodsChange.model.GoodsChange;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "goodsChange")
@Qualifier("goodsChangeDao")
public interface GoodsChangeDao extends BaseDao<GoodsChange, String> {

}