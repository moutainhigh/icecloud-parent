package com.icetech.datacenter.service.mqtt;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.icetech.datacenter.common.config.MqttConfig;
import org.apache.commons.codec.binary.Base64;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import static org.eclipse.paho.client.mqttv3.MqttConnectOptions.MQTT_VERSION_3_1_1;


/**
 *发送MQTT消息
 *
 * @author fangct
 */
@Component
public class MqttSendServer {

    @Autowired
    private MqttConfig mqttConfig;

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String TAGS = "MQ2MQTT";

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 2; i++) {
            long millis = System.currentTimeMillis();
            new MqttSendServer().sendMessage("a", "test");
            System.out.println("用时：" + (System.currentTimeMillis() - millis));
            Thread.sleep(1500);
        }
        System.out.println("结束-------");
    }

    /**
     * 发送MQ消息
     *
     * @param parkCode
     * @param msgBody
     * @return
     */
    public boolean sendMessage(String parkCode, String msgBody) {
        try {
            MqttClient mqttClient = getMqttClient(parkCode);
            return send(mqttClient, parkCode, msgBody);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 发送MQ消息
     *
     * @param mqttClient
     * @return
     */
    private boolean send(MqttClient mqttClient, String parkCode, String msgBody) {
        String topic = mqttConfig.getParkTopic().replace("?", parkCode);
        if (!topic.equals("topic-P1539928451")) {
            topic = "topic-park-test";
        }

        try {
            //sync send normal pub sub msg
            final String mqttSendTopic = topic;// + "/qos" + qosLevel;
            MqttMessage message = new MqttMessage(msgBody.getBytes());
            message.setQos(0);
            mqttClient.publish(mqttSendTopic, message);
            logger.info("【MQ发送消息】>>>> 消息内容：{}", msgBody);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取发送对象
     *
     * @param parkCode
     * @return
     */
    private MqttClient getMqttClient(String parkCode) throws Exception {
        final MemoryPersistence memoryPersistence = new MemoryPersistence();
        MqttClient mqttClient = null;

        if (MqttClientMap.get(parkCode) != null) {
            logger.info("<MQ发送> MqttClient:{}, 已创建过MqttClient对象，直接从Map中读取...", parkCode);
            mqttClient = MqttClientMap.get(parkCode);
        } else {
            logger.info("<MQ发送> MqttClient:{}, 第一次创建Producer对象...", parkCode);
            String accessKey = mqttConfig.getAccessKey();
            String secretKey = mqttConfig.getSecretKey();
            String brokerUrl = mqttConfig.getBrokerUrl();
            String groupId = mqttConfig.getGroupId();
            boolean cleanSession = Boolean.parseBoolean(mqttConfig.getCleanSession());
            Properties initProperties = new Properties();
            initProperties.put(PropertyKeyConst.AccessKey, accessKey);
            initProperties.put(PropertyKeyConst.SecretKey, secretKey);


            mqttClient = new MqttClient(brokerUrl, groupId + "@@@0001", memoryPersistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            //cal the sign as password,sign=BASE64(MAC.SHA1(groupId,secretKey))
            String sign = null;
            sign = macSignature(groupId, secretKey);
            connOpts.setUserName(accessKey);
            connOpts.setPassword(sign.toCharArray());
            connOpts.setCleanSession(cleanSession);
            connOpts.setKeepAliveInterval(90);
            connOpts.setAutomaticReconnect(true);
            connOpts.setMqttVersion(MQTT_VERSION_3_1_1);
            mqttClient.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                    System.out.println("connect success");
                }

                @Override
                public void connectionLost(Throwable throwable) {
                    throwable.printStackTrace();
                }

                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    System.out.println("receive msg from topic " + s + " , body is " + new String(mqttMessage.getPayload()));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    //this notice make sense when qos >0
                    System.out.println("send msg succeed");
                }
            });
            mqttClient.connect(connOpts);
            //保存mqttClient
            MqttClientMap.put(parkCode, mqttClient);
        }
        return mqttClient;
    }

    /**
     * @param text      要签名的文本
     * @param secretKey 阿里云MQ secretKey
     * @return 加密后的字符串
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    public static String macSignature(String text, String secretKey) throws InvalidKeyException, NoSuchAlgorithmException {
        Charset charset = Charset.forName("UTF-8");
        String algorithm = "HmacSHA1";
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(secretKey.getBytes(charset), algorithm));
        byte[] bytes = mac.doFinal(text.getBytes(charset));
        return new String(Base64.encodeBase64(bytes), charset);
    }
}
