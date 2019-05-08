package com.kingpivot.common.jms.impl;

import com.kingpivot.common.jms.SendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Queue;

@Service
public class SendMessageServiceImpl implements SendMessageService {
    @Resource
    private JmsMessagingTemplate jms;
    @Autowired
    private Queue memberLogQueue;
    @Autowired
    private Queue memberLoginQueue;
    @Autowired
    private Queue zmPaySuccessQueueName;

    @Override
    public void sendMemberLogMessage(String msg) {
        jms.convertAndSend(memberLogQueue, msg);
    }

    @Override
    public void sendMemberLoginMessage(String msg) {
        jms.convertAndSend(memberLoginQueue, msg);
    }

    @Override
    public void sendZmPaySuccessMessage(String msg) {
        jms.convertAndSend(zmPaySuccessQueueName, msg);
    }
}
