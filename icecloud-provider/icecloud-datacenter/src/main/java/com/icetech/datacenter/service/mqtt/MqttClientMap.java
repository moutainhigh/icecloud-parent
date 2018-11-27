/**
 * 
 */
package com.icetech.datacenter.service.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fangct 
 * created on 2017年11月7日
 */
public class MqttClientMap {
	/**
	 * MQTT下发的车场MqttClient对象集合
	 */
	private static final Map<String, MqttClient> MQTTCLIENT_MAP = new HashMap<String, MqttClient>();

    /**
     * 存放Producer
     * @param key
     * @param mqttClient
     */
	public static void put(String key , MqttClient mqttClient){
		MQTTCLIENT_MAP.put(key, mqttClient);
	}

    /**
     * 获取Producer
     * @param key
     * @return
     */
	public static MqttClient get(String key){
		return MQTTCLIENT_MAP.get(key);
	}

    /**
     * 移除Producer
     * @param key
     * @return
     */
	public static MqttClient remove(String key){
		return MQTTCLIENT_MAP.remove(key);
	}

}
