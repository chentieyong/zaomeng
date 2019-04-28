package com.kingpivot.base.smsTemplate.service.impl;

import com.kingpivot.base.smsTemplate.dao.SmsTemplateDao;
import com.kingpivot.base.smsTemplate.model.SmsTemplate;
import com.kingpivot.base.smsTemplate.service.SmsTemplateService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("smsTemplateService")
public class SmsTemplateServiceImpl extends BaseServiceImpl<SmsTemplate, String> implements SmsTemplateService {
    @Resource(name = "smsTemplateDao")
    private SmsTemplateDao smsTemplateDao;

    @Override
    public BaseDao<SmsTemplate, String> getDAO() {
        return this.smsTemplateDao;
    }

    @Override
    public SmsTemplate getSmsTemplateBySmsWayIDAndTemplateValue(String smsWayID, String templateValue) {
        return smsTemplateDao.getSmsTemplateBySmsWayIDAndTemplateValue(smsWayID, templateValue);
    }
}
