package com.kingpivot.base.sms.dao;

import com.kingpivot.base.sms.model.SMS;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;
import java.math.BigInteger;
import java.sql.Timestamp;

@Repository
@Table(name = "sms")
@Qualifier("smsDao")
public interface SMSDao extends BaseDao<SMS, String> {
    @Query(value = "SELECT COUNT(1) FROM sms WHERE receiverNumber=?1 AND Name=?2 AND DATE_FORMAT(createdTime,'%Y-%m-%d')=" +
            "DATE_FORMAT(NOW(),'%Y-%m-%d') AND isValid=1 AND isLock=0", nativeQuery = true)
    BigInteger getTodayCount(String phone, String name);

    @Query(value = "SELECT sendDate FROM sms WHERE receiverNumber=?1 AND `Name`=?2 AND isValid=1 AND isLock=0 ORDER BY sendDate DESC LIMIT ?3,?4", nativeQuery = true)
    Timestamp getPerSms(String phone, String name, int s, int e);
}
