package com.kingpivot.base.sms.service;

import com.kingpivot.base.sms.model.SMS;
import com.kingpivot.common.service.BaseService;

import java.sql.Timestamp;

public interface SMSService extends BaseService<SMS, String> {
    int getTodayCount(String phone,String name);

    Timestamp getPerSms(String phone, String name, int s, int e);
}
