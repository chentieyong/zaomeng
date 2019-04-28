package com.kingpivot.base.smsWay.service.impl;

import com.kingpivot.base.smsWay.dao.SmsWayDao;
import com.kingpivot.base.smsWay.model.SmsWay;
import com.kingpivot.base.smsWay.service.SmsWayService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2016/9/18.
 */
@Service("smsWayService")
public class SmsWayServiceImpl extends BaseServiceImpl<SmsWay, String> implements SmsWayService {
    @Resource(name = "smsWayDao")
    private SmsWayDao smsWayDao;

    @Override
    public BaseDao<SmsWay, String> getDAO() {
        return this.smsWayDao;
    }
}
