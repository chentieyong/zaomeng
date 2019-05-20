package com.kingpivot.base.message.dao;

import com.kingpivot.base.message.model.Message;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "message")
@Qualifier("messageDao")
public interface MessageDao extends BaseDao<Message, String> {
    @Query(value = "SELECT COUNT(id) FROM message WHERE isRead=0 AND receiverID=?1 AND isValid=1 AND isLock=0", nativeQuery = true)
    int getNoReadMessageNum(String memberID);
}
