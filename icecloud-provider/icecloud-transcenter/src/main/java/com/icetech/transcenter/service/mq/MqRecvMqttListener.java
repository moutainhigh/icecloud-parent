package com.icetech.transcenter.service.mq;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * MQTT消费者监听
 * @author wangzw
 */
@Component
public class MqRecvMqttListener implements MessageListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {
        try {
            // 解析mq内容
            if (null != message && message.getBody().length > 0) {
                String body = new String(message.getBody(), "utf-8");
                logger.info("【MQ监听获取body】>>>> {}",body);
            }
            return Action.CommitMessage;
        } catch (Exception e) {
            // 消费失败
            try {
                logger.error("【MQ监听】 body:{}, 消费者处理返回结果异常, {}", new String(message.getBody(), "utf-8"), e);
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            return Action.ReconsumeLater;
        }
    }
}
