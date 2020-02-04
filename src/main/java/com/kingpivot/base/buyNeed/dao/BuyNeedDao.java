package com.kingpivot.base.buyNeed.dao;

import com.kingpivot.base.buyNeed.model.BuyNeed;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "buyNeed")
@Qualifier("buyNeedDao")
public interface BuyNeedDao extends BaseDao<BuyNeed, String> {
}
