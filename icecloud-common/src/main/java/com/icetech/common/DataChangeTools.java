package com.icetech.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class DataChangeTools{
	private static Logger logger = LoggerFactory.getLogger(DataChangeTools.class);

	/***
	 * obj转自定义bean
	 * @param obj
	 * @param clazz
	 * @return
	 */
	public static <T> T convert2bean(Object obj, Class<T> clazz){
		String gson = DataChangeTools.bean2gson(obj);
		return DataChangeTools.gson2bean(gson, clazz);
	}
	/***
	 * gson 转bean
	 * @param params
	 * @param clazz
	 * @return
	 */
	public static <T> T gson2bean(String params,Class<T> clazz){
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			 gson =new GsonBuilder().serializeNulls().create();
		try {
			return gson.fromJson(params, clazz);
		} catch (Exception e) {
			logger.error("***  gson2bean 请求参数转换错误");
			logger.error("***  参数为"+params);
			logger.error("** "+e);
			return null;
		}
	}
	
	/***
	 * gson 转list
	 * @param params
	 * @param cla
	 * @return
	 */
	public static <T> List<T> gson2List(String params, Class<T> cla){
        try {
            List list = new ArrayList();
            JSONArray listj = (JSONArray) JSONArray.parse(params);
            for (int i=0;i<listj.size();i++) {
                JSONObject json = (JSONObject)listj.get(i);
                String jsonStr = json.toJSONString();
                list.add(gson2bean(jsonStr, cla));
            }
            return list;
        } catch (Exception e) {
            logger.error("***  gson2List请求参数转换错误");
            logger.error("** "+e);
            return null;
        }
	}

    /**
	 * bean转gson
	 * @param <T>
	 */
	public static <T> String bean2gson(Object object){
		if (object == null){
			return null;
		}
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        return gson.toJson(object);
	}
	
	/** 
     * 将json格式封装的字符串数据转换成java中的Map<String, String>数据
     * @return 
     */  
    public static Map<String, String> json2MapString(String params) {
		if (params == null){
			return null;
		}
    	HashMap<String, String> data = new LinkedHashMap<String, String>();
        // 将json字符串转换成jsonObject  
        JSONObject jsonObject = JSONObject.parseObject(params); 
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            String key = String.valueOf(entry.getKey());  
            String value =String.valueOf(entry.getValue());  
            data.put(key, value);  
        }
        return data;  
    }

	/**
	 * 将json格式封装的字符串数据转换成java中的Map<String, Object>数据
	 * @return
	 */
	public static Map<String, Object> json2Map(String params) {
		if (params == null){
			return null;
		}
		HashMap<String, Object> data = new LinkedHashMap<String, Object>();
		// 将json字符串转换成jsonObject
		JSONObject jsonObject = JSONObject.parseObject(params);
		for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
			String key = String.valueOf(entry.getKey());
			Object value = entry.getValue();
			data.put(key, value);
		}
		return data;
	}

	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串  
	 * @param params 需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String createLinkString(Map<String, Object> params) {
		if (params == null){
			return null;
		}

		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		String prestr = "";
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			Object value = params.get(key);
			if (value instanceof Map<?, ?>) {
				Map<?, ?> rspJson = (Map<?, ?>) value;
				value = DataChangeTools.bean2gson(rspJson);
			}
			if (value == null || String.valueOf(value).equals("")) {
				continue;
			}else {
				if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
					prestr = prestr + key + "=" + value;
				} else {
					prestr = prestr + key + "=" + value + "&";
				}
			}
		}
		if (prestr.endsWith("&")) {// 拼接时，不包括最后一个&字符
			prestr = prestr.substring(0, prestr.length() - 1);
		}
		return prestr;
	}
	/**
	 * map转gson
	 * @param map
	 */
	public static String map2JSON(Map<String, Object> map){
		List<String> keys = new ArrayList<String>(map.keySet());
		return appendJSON(map, keys);
	}
	/**
	 * map拼接gson
	 * @param map
	 */
	public static String appendJSON(Map<String, Object> map, List<String> keys){
		StringBuffer resultString = new StringBuffer();
		resultString.append('{');
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			Object value = map.get(key);
			if (ToolsUtil.isNull(value)) {
				continue;
			}else {
				resultString.append('"');
				resultString.append(key);
				resultString.append('"');
				resultString.append(':');
				if (value instanceof Number){
					resultString.append(value);
				}else {
					if (key.equals("bizContent")){
						resultString.append(value);
					}else{
						resultString.append('"');
						resultString.append(value);
						resultString.append('"');
					}
				}
				if(i < keys.size()-1) {
					resultString.append(',');
				}
			}
		}
		resultString.append('}');
		return resultString.toString();
	}
}

