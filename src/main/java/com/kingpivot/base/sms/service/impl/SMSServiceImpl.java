package com.kingpivot.base.sms.service.impl;

import com.kingpivot.base.sms.dao.SMSDao;
import com.kingpivot.base.sms.model.SMS;
import com.kingpivot.base.sms.service.SMSService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.Timestamp;


@Service("smsService")
public class SMSServiceImpl extends BaseServiceImpl<SMS, String> implements SMSService {

    @Autowired
    private SMSDao smsDao;

    @Override
    public BaseDao<SMS, String> getDAO() {
        return this.smsDao;
    }

    @Override
    public int getTodayCount(String phone, String name) {
        BigInteger val = smsDao.getTodayCount(phone, name);
        if (val == null) {
            return 0;
        }
        return val.intValue();
    }

    @Override
    public Timestamp getPerSms(String phone, String name, int s, int e) {
        return smsDao.getPerSms(phone, name, s, e);
    }
}
