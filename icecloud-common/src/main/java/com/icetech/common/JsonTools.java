package com.icetech.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;


public class JsonTools {
    private static final Logger logger = LoggerFactory.getLogger(JsonTools.class);
    /** Mapper */
    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        // 空对象不要抛出异常
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }


    /**
     * 工具类，防止被实例化
     */
    private JsonTools() {
    }

    /**
     * 转换对象为字符串
     */
    public static String toString(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error("转换对象异常", e);
        }
        return null;
    }

    /**
     * 将字符串转换为Bean对象
     */
    public static <T> T toBean(String json, Class<T> classz) {
        if (json == null) {
            return null;
        }
        try {
            return mapper.readValue(json, classz);
        } catch (IOException e) {
            logger.error("转换对象异常", e);
        }
        return null;
    }

    /**
     * 转换对象为Bean，通过JavaType定义复杂对象
     */
    public static <T> T Convert(String json, JavaType javaType) {
        if (json == null) {
            return null;
        }
        try {
            return mapper.readValue(json, javaType);
        } catch (IOException e) {
            logger.error("转换对象异常", e);
        }
        return null;
    }

    /**
     * 转换对象为Bean，通过JavaType定义复杂对象
     */
    public static <T> T Convert(String json, TypeReference<T> typeReference) {
        if (json == null) {
            return null;
        }
        try {
            return mapper.readValue(json, typeReference);
        } catch (IOException e) {
            logger.error("转换对象异常", e);
        }
        return null;
    }


    /**
     * 构造Collection类型的JavaType对象
     */
    public static JavaType createJavaType(Class<? extends Collection> collectionClass, Class<?> elementClass) {
        return mapper.getTypeFactory().constructCollectionType(collectionClass, elementClass);
    }


    /**
     * 构造Map类型的JavaType对象
     */
    public static JavaType createJavaType(Class<? extends Map> mapClass, Class<?> keyClass, Class<?> valueClass) {
        return mapper.getTypeFactory().constructMapType(mapClass, keyClass, valueClass);
    }


    /**
     * 通过获取TypeFactory从而构建更加复杂的JavaType类型
     */
    public static TypeFactory getTypeFactory() {
        return mapper.getTypeFactory();
    }


}
