package com.icetech.datacenter.service.mqtt;

import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;
import com.icetech.datacenter.common.config.MqttConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class MqttBeanConfiguration {

    @Autowired
    private MqttConfig mqttConfig;

    @Bean(initMethod="start",destroyMethod="shutdown")
    public ConsumerBean consumerBean(MqRecvMqttListener mqttListener){
        ConsumerBean consumerBean = new ConsumerBean();
        Properties properties = new Properties();
        properties.put("ConsumerId",mqttConfig.getConsumerId());
        properties.put("AccessKey",mqttConfig.getAccessKey());
        properties.put("SecretKey",mqttConfig.getSecretKey());
        properties.put("ConsumeThreadNums",mqttConfig.getThreadNums());
        Map<Subscription, MessageListener> subscriptionTable = new HashMap<>();
        Subscription subscription = new Subscription();
        subscription.setTopic(mqttConfig.getMyTopic());
        subscription.setExpression("*");
        subscriptionTable.put(subscription,mqttListener);
        consumerBean.setProperties(properties);
        consumerBean.setSubscriptionTable(subscriptionTable);
        return consumerBean;
    }

    private static String subTopic = "topic-park-test";
    private static String accessKey = "LTAIGsriNzjuQJo0";
    private static String secretKey = "E8EDk6jvFkXN0SV5NBLhHYvdAFTOQ8";
    private static String consumerId="CID-park-test";

    @Bean(initMethod="start",destroyMethod="shutdown")
    public ConsumerBean demo(VisualRecvMqttDemo visualRecvMqttDemo){
        ConsumerBean consumerBean = new ConsumerBean();
        Properties properties = new Properties();
        properties.put("ConsumerId",consumerId);
        properties.put("AccessKey",accessKey);
        properties.put("SecretKey",secretKey);
        properties.put("ConsumeThreadNums",mqttConfig.getThreadNums());
        Map<Subscription, MessageListener> subscriptionTable = new HashMap<>();
        Subscription subscription = new Subscription();
        subscription.setTopic(subTopic);
        subscription.setExpression("*");
        subscriptionTable.put(subscription,visualRecvMqttDemo);
        consumerBean.setProperties(properties);
        consumerBean.setSubscriptionTable(subscriptionTable);
        return consumerBean;
    }
}
