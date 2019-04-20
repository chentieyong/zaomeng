package com.kingpivot.base.message.dao;

import com.kingpivot.base.message.model.Message;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "message")
@Qualifier("messageDao")
public interface MessageDao extends BaseDao<Message, String> {
}
