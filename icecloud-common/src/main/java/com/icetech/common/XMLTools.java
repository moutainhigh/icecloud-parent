package com.icetech.common;

import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author fangct
 * 时间：2015年8月15日
 * 功能：处理XML的相关方法
 * 备注：
 */

public class XMLTools {
	/**
	 * 把xml字符串转成map
	 * @param xml
	 * @return
	 */
	public static Map<String, Object>xmlToMap(String xml){
		Document document;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			document = DocumentHelper.parseText(xml);
			Element root = document.getRootElement();// 获取根节点
			for (@SuppressWarnings("unchecked")
			Iterator<Element> iterator = root.elementIterator(); iterator.hasNext();) {
				Element element = iterator.next();
				if(!element.getText().equals("")){
					map.put(element.getName(), element.getText());
				}
				
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return map;
	}
	 /**
     * 将xml字符串转换为JSON字符串
     * @param xmlString xml字符串
     * @return JSON字符串对象
     */
    public static String xml2json(String xmlString) {
        XMLSerializer xmlSerializer = new XMLSerializer();
        net.sf.json.JSON json = xmlSerializer.read(xmlString);
        //使用xmlSerializer转换json字符串时，会将空值转化成了[]，为了取值时方便，需将[]替换成''
        String jsonStr = json.toString().replaceAll("\"", "\'")
        		.replaceAll("\\[\\[\\]\\]", "{}")
        		.replaceAll("\\[\\]", "''");
        return jsonStr;
    }
    /**
     * JSON字符串转换成XML字符串
     * @param jsonString
     * @return
     */
    public static String json2xml(String jsonString) {
        XMLSerializer xmlSerializer = new XMLSerializer();
        return xmlSerializer.write(JSONSerializer.toJSON(jsonString));
        // return xmlSerializer.write(JSONArray.fromObject(jsonString));//这种方式只支持JSON数组
    }
    
    /**
	 * map转换xml字符串，无属性
	 * @author fangct
	 * @param paramMap
	 * @return
	 */
	public static String map2XmlString(Map<String, String> paramMap) {
    	StringBuffer strBuf = new StringBuffer();
    	strBuf.append("<xml>");
    	Set<Entry<String,String>> entrySet = paramMap.entrySet();
    	Iterator<Entry<String, String>> iterator = entrySet.iterator();
    	while(iterator.hasNext()) {
    		Entry<String, String> entry = iterator.next();
    		strBuf.append("<" + entry.getKey() + ">");
    		strBuf.append(entry.getValue());
    		strBuf.append("</" + entry.getKey() + ">");
    	}
    	strBuf.append("</xml>");
    	return strBuf.toString();
    }
	
	/**
	 * map转换xml字符串，无属性
	 * @author fangct
	 * @param paramMap
	 * @return
	 */
	public static String map2XmlStringData(Map<String, String> paramMap) {
    	StringBuffer strBuf = new StringBuffer();
    	strBuf.append("<xml>");
    	Set<Entry<String,String>> entrySet = paramMap.entrySet();
    	Iterator<Entry<String, String>> iterator = entrySet.iterator();
    	while(iterator.hasNext()) {
    		Entry<String, String> entry = iterator.next();
    		strBuf.append("<" + entry.getKey() + ">");
    		strBuf.append("<![CDATA[");
    		strBuf.append(entry.getValue());
    		strBuf.append("]]>");
    		strBuf.append("</" + entry.getKey() + ">");
    	}
    	strBuf.append("</xml>");
    	return strBuf.toString();
    }
	
	/**
	 * xml转换为Map<String, String>
	 * @author fangct
	 * @param xml
	 * @return
	 */
	public static Map<String, String> xmlToMapString(String xml){
		Document document;
		Map<String, String> map = new HashMap<String, String>();
		try {
			document = DocumentHelper.parseText(xml);
			Element root = document.getRootElement();// 获取根节点
			for (Iterator<Element> iterator = root.elementIterator(); iterator.hasNext();) {
				Element element = iterator.next();
				if(element.getName().equals("hblist")) {
					String stringValue = element.getStringValue();
//					map.put("rcv_time", stringValue.substring(stringValue.length()-19));
					map.put("rcv_time", stringValue.substring(stringValue.lastIndexOf("201"), stringValue.lastIndexOf("201")+19));
					continue;
				}
				if(!element.getText().equals("")){
					map.put(element.getName(), element.getText());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
}
