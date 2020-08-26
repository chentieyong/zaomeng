package com.kingpivot.base.goodsShopParameterDefine.service.impl;

import com.kingpivot.base.goodsShopParameterDefine.dao.GoodsShopParameterDefineDao;
import com.kingpivot.base.goodsShopParameterDefine.model.GoodsShopParameterDefine;
import com.kingpivot.base.goodsShopParameterDefine.service.GoodsShopParameterDefineService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("goodsShopParameterDefineService")
public class GoodsShopParameterDefineServiceImpl extends BaseServiceImpl<GoodsShopParameterDefine, String> implements GoodsShopParameterDefineService {
    @Resource(name = "goodsShopParameterDefineDao")
    private GoodsShopParameterDefineDao goodsShopParameterDefineDao;

    @Override
    public BaseDao<GoodsShopParameterDefine, String> getDAO() {
        return this.goodsShopParameterDefineDao;
    }
}
