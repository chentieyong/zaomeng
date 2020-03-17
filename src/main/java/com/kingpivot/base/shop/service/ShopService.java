package com.kingpivot.base.shop.service;

import com.kingpivot.base.shop.model.Shop;
import com.kingpivot.common.service.BaseService;

public interface ShopService extends BaseService<Shop, String> {
    String getNameById(String id);
}
