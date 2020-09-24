package com.kingpivot.base.shopStatistics.service;

import com.kingpivot.base.shopStatistics.model.ShopStatistics;
import com.kingpivot.common.service.BaseService;

public interface ShopStatisticsService extends BaseService<ShopStatistics, String> {

    ShopStatistics getShopStatisticsByShopID(String shopID);

}
