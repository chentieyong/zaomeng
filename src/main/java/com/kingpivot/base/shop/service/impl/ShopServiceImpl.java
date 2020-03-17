package com.kingpivot.base.shop.service.impl;

import com.kingpivot.base.shop.dao.ShopDao;
import com.kingpivot.base.shop.model.Shop;
import com.kingpivot.base.shop.service.ShopService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("shopService")
public class ShopServiceImpl extends BaseServiceImpl<Shop, String> implements ShopService {

    @Resource(name = "shopDao")
    private ShopDao shopDao;

    @Override
    public BaseDao<Shop, String> getDAO() {
        return this.shopDao;
    }

    @Override
    public String getNameById(String id) {
        return shopDao.getNameById(id);
    }
}
