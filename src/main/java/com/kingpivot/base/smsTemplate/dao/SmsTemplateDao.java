package com.kingpivot.base.smsTemplate.dao;

import com.kingpivot.base.smsTemplate.model.SmsTemplate;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "smsTemplate")
@Qualifier("smsTemplateDao")
public interface SmsTemplateDao extends BaseDao<SmsTemplate, String> {
    @Query(value = "SELECT * FROM smsTemplate WHERE smsWayID=?1 AND templateValue=?2 AND isValid=1 limit 1",nativeQuery = true)
    SmsTemplate getSmsTemplateBySmsWayIDAndTemplateValue(String smsWayID,String templateValue);
}
