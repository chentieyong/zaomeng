package com.kingpivot.base.goodsChange.service.impl;

import com.kingpivot.base.goodsChange.dao.GoodsChangeDao;
import com.kingpivot.base.goodsChange.model.GoodsChange;
import com.kingpivot.base.goodsChange.service.GoodsChangeService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("goodsChangeService")
public class GoodsChangeServiceImpl extends BaseServiceImpl<GoodsChange, String> implements GoodsChangeService {

    @Resource(name = "goodsChangeDao")
    private GoodsChangeDao goodsChangeDao;

    @Override
    public BaseDao<GoodsChange, String> getDAO() {
        return this.goodsChangeDao;
    }
}

