package com.icetech.common;

import com.icetech.common.domain.request.BaseRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class SignTools {

    public static Logger logger = LoggerFactory.getLogger(SignTools.class);

    /**
     * MD5验签
     * @param baseRequest 验签参数
     * @param key 密钥
     * @return
     */
    public static boolean verifyMD5Sign(BaseRequest baseRequest , String key){
        String sign = "";
        String mySign = "";
        String linkString = "";
        try {
            Map<String, Object> map = SignTools.convertMap(baseRequest);
            sign = (String) map.get("sign");
            map.remove("sign");
            linkString = getSignContent(map, key);
            mySign = MD5encryptTool.getMD5(linkString);

            if (sign.equals(mySign)){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("<签名校验> MD5验签不通过，md5的字符串：{}，sign：{}，mySign：{}", linkString, sign, mySign);
        return false;
    }


    /**
     * 生成MD5签名
     * @param params
     * @param key
     * @return
     */
    public static String getMySign(Map<String, Object> params, String key) {
        String linkString = "";
        if(null != params && !"".equals(params)) {
            linkString = getSignContent(params, key) ;
            logger.debug("<签名校验> 参与签名生成的字符串为：{}", linkString);
            String mySign = null;
            try {
                mySign = MD5encryptTool.getMD5(linkString).toUpperCase();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mySign;
        }else{
            return null;
        }
    }

    /**
     *得到签名内容
     * @param sortedParams
     * @return
     */
    public static String getSignContent(Map<String, Object> sortedParams, String secretKey) {
        StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList<String>(sortedParams.keySet());
        Collections.sort(keys);
        int index = 0;
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            Object value = sortedParams.get(key);
            if (value != null && String.valueOf(value).length() > 0) {
                content.append((index == 0 ? "" : "&") + key + "=" + value);
                index++;
            }
        }
        content.append("&key=" + secretKey);
        return content.toString();
    }

    /**
     * map转有序gson
     * @param map
     */
    public static String map2SortJSON(Map<String, Object> map){
        List<String> keys = new ArrayList<String>(map.keySet());
        Collections.sort(keys);
        return DataChangeTools.appendJSON(map, keys);
    }

    /**
     * 将一个 JavaBean 对象转化为一个 Map, bizContent类转换成json后放入map
     *
     * @param bean
     *            要转化的JavaBean 对象
     * @return 转化出来的 Map 对象
     * @throws IntrospectionException
     *             如果分析类属性失败
     * @throws IllegalAccessException
     *             如果实例化 JavaBean 失败
     * @throws InvocationTargetException
     *             如果调用属性的 setter 方法失败
     */
    public static Map<String, Object> convertMap(Object bean) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Class type = bean.getClass();
        Map<String, Object> returnMap = new LinkedHashMap<String, Object>();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);

        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    if (result instanceof Map<?,?>){
                        result = DataChangeTools.bean2gson(result);
                    }else if (propertyName.equals("bizContent")){
                        result = DataChangeTools.bean2gson(result);
                    }
                    returnMap.put(propertyName, result);
                }
            }
        }
        return returnMap;
    }
}
