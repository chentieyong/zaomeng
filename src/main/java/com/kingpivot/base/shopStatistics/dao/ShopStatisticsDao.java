package com.kingpivot.base.shopStatistics.dao;

import com.kingpivot.base.shopStatistics.model.ShopStatistics;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "shopStatistics")
@Qualifier("shopStatisticsDao")
public interface ShopStatisticsDao extends BaseDao<ShopStatistics, String> {
    @Query(value = "select * from shopStatistics where shopID=?1 and isValid=1 and isLock=0", nativeQuery = true)
    ShopStatistics getShopStatisticsByShopID(String shopID);
}





