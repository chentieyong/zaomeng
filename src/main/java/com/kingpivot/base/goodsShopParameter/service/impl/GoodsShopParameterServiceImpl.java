package com.kingpivot.base.goodsShopParameter.service.impl;

import com.kingpivot.base.goodsShopParameter.dao.GoodsShopParameterDao;
import com.kingpivot.base.goodsShopParameter.model.GoodsShopParameter;
import com.kingpivot.base.goodsShopParameter.service.GoodsShopParameterService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("goodsShopParameterService")
public class GoodsShopParameterServiceImpl extends BaseServiceImpl<GoodsShopParameter, String> implements GoodsShopParameterService {
    @Resource(name = "goodsShopParameterDao")
    private GoodsShopParameterDao goodsShopParameterDao;

    @Override
    public BaseDao<GoodsShopParameter, String> getDAO() {
        return this.goodsShopParameterDao;
    }
}
