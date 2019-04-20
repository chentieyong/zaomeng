package com.kingpivot.base.message.service.impl;

import com.kingpivot.base.message.dao.MessageDao;
import com.kingpivot.base.message.model.Message;
import com.kingpivot.base.message.service.MessageService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("messageService")
public class MessageServiceImpl extends BaseServiceImpl<Message, String> implements MessageService {

    @Resource(name = "messageDao")
    private MessageDao MessageDao;

    @Override
    public BaseDao<Message, String> getDAO() {
        return this.MessageDao;
    }

}
