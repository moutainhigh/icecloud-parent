/**
 * 
 */
package com.icetech.common.constants;

/**
 * @author fangct 
 * created on 2017年11月13日
 */
public class TimeOutConstants {

	/**
	 * 放入REDIS返回数据的超时时间，单位秒 ，5分钟
	 */
	public static final Long REDIS_TIMEOUT = 300L;
	/**
	 * 本地失联超时上限，单位秒，6分钟
	 */
	public static final int OFF_LINE_TIME = 6 * 60;
	
}
