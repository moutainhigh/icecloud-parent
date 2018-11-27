package com.icetech.transcenter.service.mq;

import com.aliyun.openservices.ons.api.*;
import com.icetech.transcenter.config.mq.TransCenterMqttConfig;
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
    private TransCenterMqttConfig mqttConfig;

    /**
     * 发送MQ消息
     * @param pId
     * @param msgBody
     * @return
     */
    public boolean sendMessage(String pId, String msgBody){
        Producer producer = getProducer(pId);
        SendResult result = send(producer, msgBody);
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
     * @return
     */
    private SendResult send(Producer producer,String msgBody){
        final String topic = mqttConfig.getPubTopic();
        final Integer qosLevel = mqttConfig.getQos();
        final Boolean cleanSession = mqttConfig.getCleanSession();
        final Message msg = new Message(topic, TAGS, msgBody.getBytes());
//        msg.putUserProperties("mqttSecondTopic",  "/qos" + qosLevel);
        msg.putUserProperties("qoslevel", String.valueOf(qosLevel));
        msg.putUserProperties("cleansessionflag", String.valueOf(cleanSession));
        logger.info("【MQ发送消息】>>>> 消息内容：{}",msgBody);
        SendResult result = producer.send(msg);
        return result;
    }

    /**
     * 获取发送对象
     * @param uid
     * @return
     */
    private Producer getProducer(String uid) {
        Producer producer;
        if (ProducerMap.get(uid) != null) {
            logger.info("<MQ发送> Parkcode:{}, 已创建过Producer对象，直接从Map中读取...", uid);
            producer = ProducerMap.get(uid);
            if (producer.isClosed()) {
                producer.start();
            }
        } else {
            logger.info("<MQ发送> Parkcode:{}, 第一次创建Producer对象...", uid);
            String producerId = mqttConfig.getProducerId();
            String accessKey = mqttConfig.getAccessKey();
            String secretKey = mqttConfig.getSecretKey();
            Properties initProperties = new Properties();
            initProperties.put(PropertyKeyConst.ProducerId, producerId);
            initProperties.put(PropertyKeyConst.AccessKey, accessKey);
            initProperties.put(PropertyKeyConst.SecretKey, secretKey);
            producer = ONSFactory.createProducer(initProperties);
            producer.start();
            //保存Producer
            ProducerMap.put(uid, producer);
        }
        return producer;
    }

}
