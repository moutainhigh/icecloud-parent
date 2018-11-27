package com.icetech.datacenter.service.mqtt;

import com.aliyun.openservices.ons.api.*;
import com.icetech.datacenter.common.config.MqttConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * 将消息发送到MQTT
 * @author fangct
 */
@Component
public class MqSendMqttServer {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String TAGS = "MQ2MQTT";

    @Autowired
    private MqttConfig mqttConfig;

    /**
     * 发送MQ消息
     * @param parkCode
     * @param msgBody
     * @return
     */
    public boolean sendMessage(String parkCode, String msgBody){
        Producer producer = getProducer(parkCode);
        SendResult result = send(producer, parkCode, msgBody);
        logger.info("<MQ发送> 完成，result：{}", result);
        if (result != null && result.getMessageId() != null){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 发送MQ消息
     * @param producer
     * @param parkCode
     * @return
     */
    private SendResult send(Producer producer, String parkCode, String msgBody){
        String topic = mqttConfig.getParkTopic().replace("?", parkCode);
        if (!topic.equals("topic-P1539928451")){
            topic = "topic-park-test";
        }
        final int qosLevel = Integer.parseInt(mqttConfig.getQos());
        final Boolean cleanSession = Boolean.parseBoolean(mqttConfig.getCleanSession());
        final Message msg = new Message(
                topic,//the topic is mqtt parent topic
                TAGS,//MQ Tag,must set MQ2MQTT
                msgBody.getBytes());//mqtt msg body
        //send mormal mqtt msg ,set the property "mqttSecondTopic={{your mqtt subTopic}}"
//        msg.putUserProperties("mqttSecondTopic", "/qos" + qosLevel);
        //mq send mqtt msg ,the qos default =1
        msg.putUserProperties("qoslevel", String.valueOf(qosLevel));
        //mq send mqtt msg ,the cleansession default set true
        msg.putUserProperties("cleansessionflag", String.valueOf(cleanSession));
        SendResult result = producer.send(msg);
        return result;
    }

    /**
     * 获取发送对象
     * @param parkCode
     * @return
     */
    private Producer getProducer(String parkCode) {
        Producer producer = null;
        if (ProducerMap.get(parkCode) != null) {
            logger.info("<MQ发送> Parkcode:{}, 已创建过Producer对象，直接从Map中读取...", parkCode);
            producer = ProducerMap.get(parkCode);
            if (producer.isClosed()) {
                producer.start();
            }
        } else {
            logger.info("<MQ发送> Parkcode:{}, 第一次创建Producer对象...", parkCode);
            String producerId = mqttConfig.getProducerId();
            if (!producerId.equals("PID-P1539928451")){
                producerId = "PID-park-test";
            }
            String accessKey = mqttConfig.getAccessKey();
            String secretKey = mqttConfig.getSecretKey();
            Properties initProperties = new Properties();
            initProperties.put(PropertyKeyConst.ProducerId, producerId);
            initProperties.put(PropertyKeyConst.AccessKey, accessKey);
            initProperties.put(PropertyKeyConst.SecretKey, secretKey);
            producer = ONSFactory.createProducer(initProperties);
            producer.start();
            //保存Producer
            ProducerMap.put(parkCode, producer);
        }
        return producer;
    }

}
