package com.icetech.transcenter.config.mq;

import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;
import com.icetech.transcenter.service.mq.MqRecvMqttListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 消费者mq的配置
 */
@Configuration
public class MqttBeanConfiguration {

    @Autowired
    private TransCenterMqttConfig mqttConfig;

    @Bean(initMethod="start",destroyMethod="shutdown")
    public ConsumerBean consumerBean(MqRecvMqttListener mqttListener){
        ConsumerBean consumerBean = new ConsumerBean();
        Properties properties = new Properties();
        properties.put("ConsumerId",mqttConfig.getConsumerId());
        properties.put("AccessKey",mqttConfig.getAccessKey());
        properties.put("SecretKey" ,mqttConfig.getSecretKey());
        properties.put("ConsumeThreadNums",mqttConfig.getThreadNums());
        Map<Subscription, MessageListener> subscriptionTable = new HashMap<>();
        Subscription subscription = new Subscription();
        subscription.setTopic(mqttConfig.getSubTopic());
        subscription.setExpression("*");
        subscriptionTable.put(subscription,mqttListener);
        consumerBean.setProperties(properties);
        consumerBean.setSubscriptionTable(subscriptionTable);
        return consumerBean;
    }

}
