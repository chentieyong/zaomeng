package com.kingpivot.base.buyNeed.service.impl;

import com.kingpivot.base.buyNeed.dao.BuyNeedDao;
import com.kingpivot.base.buyNeed.model.BuyNeed;
import com.kingpivot.base.buyNeed.service.BuyNeedService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("buyNeedService")
public class BuyNeedServiceImpl extends BaseServiceImpl<BuyNeed, String> implements BuyNeedService {
    @Autowired
    private BuyNeedDao buyNeedDao;

    @Override
    public BaseDao<BuyNeed, String> getDAO() {
        return this.buyNeedDao;
    }
}
