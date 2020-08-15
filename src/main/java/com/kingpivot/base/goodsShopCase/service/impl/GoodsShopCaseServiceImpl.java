package com.kingpivot.base.goodsShopCase.service.impl;

import com.kingpivot.base.goodsShopCase.dao.GoodsShopCaseDao;
import com.kingpivot.base.goodsShopCase.model.GoodsShopCase;
import com.kingpivot.base.goodsShopCase.service.GoodsShopCaseService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("goodsShopCaseService")
public class GoodsShopCaseServiceImpl extends BaseServiceImpl<GoodsShopCase, String> implements GoodsShopCaseService {
    @Resource(name = "goodsShopCaseDao")
    private GoodsShopCaseDao goodsShopCaseDao;


    @Override
    public BaseDao<GoodsShopCase, String> getDAO() {
        return this.goodsShopCaseDao;
    }
}
