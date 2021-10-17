package com.kingpivot.common.jms;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import javax.jms.Queue;

@Configuration
public class ActiveMQConfig {
    @Value("${memberLogQueueName}")
    private String memberLogQueueName;

    @Value("${memberLoginQueueName}")
    private String memberLoginQueueName;

    @Value("${zmPaySuccessQueueName}")
    private String zmPaySuccessQueueName;

    @Value("${getMemberBonusQueueName}")
    private String getMemberBonusQueueName;

    @Value("${addAttachmentMessage}")
    private String addAttachmentMessage;

    @Value("${sendMessageQueueName}")
    private String sendMessage;

    @Value("${sendUsePointQueueName}")
    private String sendUsePointQueueName;

    @Value("${sendGetPointQueueName}")
    private String sendGetPointQueueName;

    @Value("${sendMemberBalanceQueueName}")
    private String sendMemberBalanceQueueName;

    @Value("${sendHbShopBuyGoodsQueueName}")
    private String sendHbShopBuyGoodsQueueName;

    @Value("${sendShopBuyGoodsQueueName}")
    private String sendShopBuyGoodsQueueName;

    @Value("${spring.activemq.user}")
    private String usrName;

    @Value("${spring.activemq.password}")
    private String password;

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    @Bean
    public Queue memberLogQueue() {
        return new ActiveMQQueue(memberLogQueueName);
    }

    @Bean
    public Queue memberLoginQueue() {
        return new ActiveMQQueue(memberLoginQueueName);
    }

    @Bean
    public Queue zmPaySuccessQueueName() {
        return new ActiveMQQueue(zmPaySuccessQueueName);
    }

    @Bean
    public Queue getMemberBonusQueueName() {
        return new ActiveMQQueue(getMemberBonusQueueName);
    }

    @Bean
    public Queue addAttachmentMessage() {
        return new ActiveMQQueue(addAttachmentMessage);
    }

    @Bean
    public Queue sendMessage() {
        return new ActiveMQQueue(sendMessage);
    }

    @Bean
    public Queue sendUsePointQueueName() {
        return new ActiveMQQueue(sendUsePointQueueName);
    }

    @Bean
    public Queue sendGetPointQueueName() {
        return new ActiveMQQueue(sendGetPointQueueName);
    }

    @Bean
    public Queue sendMemberBalanceQueueName() {
        return new ActiveMQQueue(sendMemberBalanceQueueName);
    }

    @Bean
    public Queue sendHbShopBuyGoodsQueueName() {
        return new ActiveMQQueue(sendHbShopBuyGoodsQueueName);
    }

    @Bean
    public Queue sendShopBuyGoodsQueueName() {
        return new ActiveMQQueue(sendShopBuyGoodsQueueName);
    }

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory(usrName, password, brokerUrl);
    }

    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerQueue(ActiveMQConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        bean.setConnectionFactory(connectionFactory);
        return bean;
    }

    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerTopic(ActiveMQConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        //设置为发布订阅方式, 默认情况下使用的生产消费者方式
        bean.setPubSubDomain(true);
        bean.setConnectionFactory(connectionFactory);
        return bean;
    }
}
