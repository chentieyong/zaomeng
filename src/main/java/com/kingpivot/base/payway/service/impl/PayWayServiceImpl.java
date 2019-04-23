package com.kingpivot.base.payway.service.impl;

import com.kingpivot.base.payway.dao.PayWayDao;
import com.kingpivot.base.payway.model.PayWay;
import com.kingpivot.base.payway.service.PayWayService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("payWayService")
public class PayWayServiceImpl extends BaseServiceImpl<PayWay, String> implements PayWayService {

    @Resource(name = "payWayDao")
    private PayWayDao paywayDao;

    @Override
    public BaseDao<PayWay, String> getDAO() {
        return this.paywayDao;
    }
}
