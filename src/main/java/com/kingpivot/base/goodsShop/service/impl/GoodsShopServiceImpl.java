package com.kingpivot.base.goodsShop.service.impl;

import com.kingpivot.base.goodsShop.dao.GoodsShopDao;
import com.kingpivot.base.goodsShop.model.GoodsShop;
import com.kingpivot.base.goodsShop.service.GoodsShopService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2016/9/18.
 */
@Service("goodsShopService")
public class GoodsShopServiceImpl extends BaseServiceImpl<GoodsShop, String> implements GoodsShopService {
    @Resource(name = "goodsShopDao")
    private GoodsShopDao goodsShopDao;


    @Override
    public BaseDao<GoodsShop, String> getDAO() {
        return this.goodsShopDao;
    }
}
