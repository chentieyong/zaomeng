package com.kingpivot.base.message.service;

import com.kingpivot.base.message.model.Message;
import com.kingpivot.common.service.BaseService;

public interface MessageService extends BaseService<Message, String> {
    int getNoReadMessageNum(String memberID);

}
