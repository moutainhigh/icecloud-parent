package com.icetech.transcenter.config.mq;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * MQTT配置
 */
@ConfigurationProperties(prefix = "transcenter.mqtt")
@Data
@Component
public class TransCenterMqttConfig {

    /**
     * 发布主题
     */
    private String pubTopic;
    /**
     * 订阅主题
     */
    private String subTopic;
    /**
     * accessKey
     */
    private String accessKey;
    /**
     * secretKey
     */
    private String secretKey;
    /**
     * QoS （Quality of Service）指代消息传输的服务质量。它包括 QoS0（最多分发一次）、QoS1（至少达到一次）和 QoS2（仅分发一次）三种级别。
     */
    private Integer qos;
    /**
     * cleanSession 标志是 MQTT 协议中对一个客户端建立 TCP 连接后是否关心之前状态的定义。具体语义如下：
     * cleanSession=true：客户端再次上线时，将不再关心之前所有的订阅关系以及离线消息。
     * cleanSession=false：客户端再次上线时，还需要处理之前的离线消息，而之前的订阅关系也会持续生效。
     */
    private Boolean cleanSession;

    /**
     * 生产者ID
     */
    private String producerId;
    /**
     * 消费者id
     */
    private String consumerId;
    /**
     * 初始化线程
     */
    private String threadNums;
}
