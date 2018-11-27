package com.icetech.datacenter.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * MQTT配置
 */
@ConfigurationProperties(prefix = "mqtt")
@Data
@Component
public class MqttConfig {

    /**
     * broker访问地址
     */
    private String brokerUrl;

    /**
     * 主题
     */
    private String parkTopic;
    private String myTopic;
    private String accessKey;
    private String secretKey;
    private String qos;
    private String cleanSession;
    private String threadNums;
    private String groupId;

    /**
     * 生产者ID
     */
    private String producerId;
    /**
     * 消费者ID
     */
    private String consumerId;
}
