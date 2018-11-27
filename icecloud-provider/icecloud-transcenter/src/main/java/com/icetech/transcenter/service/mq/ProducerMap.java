/**
 * 
 */
package com.icetech.transcenter.service.mq;

import com.aliyun.openservices.ons.api.Producer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fangct 
 * created on 2017年11月7日
 */
public class ProducerMap {
	/**
	 * MQ下发的车场Producer对象集合
	 */
	private static final Map<String, Producer> PRODUCER_MAP = new HashMap<String, Producer>();

    /**
     * 存放Producer
     * @param key
     * @param producer
     */
	public static void put(String key , Producer producer){
		PRODUCER_MAP.put(key, producer);
	}

    /**
     * 获取Producer
     * @param key
     * @return
     */
	public static Producer get(String key){
		return PRODUCER_MAP.get(key);
	}

    /**
     * 移除Producer
     * @param key
     * @return
     */
	public static Producer remove(String key){
		return PRODUCER_MAP.remove(key);
	}

}
