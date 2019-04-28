package com.kingpivot.base.smsTemplate.service;

import com.kingpivot.base.smsTemplate.model.SmsTemplate;
import com.kingpivot.common.service.BaseService;

public interface SmsTemplateService extends BaseService<SmsTemplate, String> {
    SmsTemplate getSmsTemplateBySmsWayIDAndTemplateValue(String smsWayID,String templateValue);
}
