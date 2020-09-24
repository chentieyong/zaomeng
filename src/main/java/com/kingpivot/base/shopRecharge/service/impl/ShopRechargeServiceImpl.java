package com.kingpivot.base.shopRecharge.service.impl;

import com.kingpivot.base.shop.dao.ShopDao;
import com.kingpivot.base.shop.model.Shop;
import com.kingpivot.base.shopRecharge.dao.ShopRechargeDao;
import com.kingpivot.base.shopRecharge.model.ShopRecharge;
import com.kingpivot.base.shopRecharge.service.ShopRechargeService;
import com.kingpivot.base.shopStatistics.dao.ShopStatisticsDao;
import com.kingpivot.base.shopStatistics.model.ShopStatistics;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import com.kingpivot.common.utils.NumberUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

@Service("shopRechargeService")
public class ShopRechargeServiceImpl extends BaseServiceImpl<ShopRecharge, String> implements ShopRechargeService {
    @Resource(name = "shopRechargeDao")
    private ShopRechargeDao shopRechargeDao;
    @Resource(name = "shopStatisticsDao")
    private ShopStatisticsDao shopStatisticsDao;
    @Resource(name = "shopDao")
    private ShopDao shopDao;

    @Override
    public BaseDao<ShopRecharge, String> getDAO() {
        return this.shopRechargeDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doAuditShopRecharge(ShopRecharge shopRecharge) {
        Shop shop = shopDao.findOne(shopRecharge.getShopID());
        if (shop == null) {
            return;
        }
        //处理店铺统计数据
        ShopStatistics shopStatistics = shopStatisticsDao.getShopStatisticsByShopID(shopRecharge.getShopID());
        if (shopStatistics == null) {
            shopStatistics = new ShopStatistics();
            shopStatistics.setName(shop.getName() + "-统计信息");
            shopStatistics.setShopID(shop.getId());
            shopStatistics.setApplicationID(shopRecharge.getApplicationID());
            shopStatistics.setCashBalance(shopRecharge.getAmount());
            shopStatistics.setCashTotalRecharge(shopRecharge.getRechargeAmount());
            shopStatistics.setCreatedTime(new Timestamp(System.currentTimeMillis()));
            shopStatisticsDao.save(shopStatistics);
        }
        shopStatistics.setCashBalance(NumberUtils.keepPrecision(shopStatistics.getCashBalance() + shopRecharge.getAmount(), 2));
        shopStatistics.setCashTotalRecharge(NumberUtils.keepPrecision(shopStatistics.getCashTotalRecharge() + shopRecharge.getRechargeAmount(), 2));

        shopRechargeDao.save(shopRecharge);
    }
}
