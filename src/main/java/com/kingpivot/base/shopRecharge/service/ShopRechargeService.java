package com.kingpivot.base.shopRecharge.service;

import com.kingpivot.base.shopRecharge.model.ShopRecharge;
import com.kingpivot.common.service.BaseService;

public interface ShopRechargeService extends BaseService<ShopRecharge, String> {
    void doAuditShopRecharge(ShopRecharge shopRecharge);
}
