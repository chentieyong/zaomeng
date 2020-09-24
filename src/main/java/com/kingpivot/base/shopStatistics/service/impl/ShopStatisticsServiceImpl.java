package com.kingpivot.base.shopStatistics.service.impl;

import com.kingpivot.base.shopStatistics.dao.ShopStatisticsDao;
import com.kingpivot.base.shopStatistics.model.ShopStatistics;
import com.kingpivot.base.shopStatistics.service.ShopStatisticsService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("shopStatisticsService")
public class ShopStatisticsServiceImpl extends BaseServiceImpl<ShopStatistics, String> implements ShopStatisticsService {

    @Resource(name = "shopStatisticsDao")
    private ShopStatisticsDao shopStatisticsDao;

    @Override
    public BaseDao<ShopStatistics, String> getDAO() {
        return this.shopStatisticsDao;
    }

    @Override
    public ShopStatistics getShopStatisticsByShopID(String shopID) {
        return shopStatisticsDao.getShopStatisticsByShopID(shopID);
    }
}
