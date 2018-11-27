package com.icetech.common;

import com.icetech.common.constants.CodeConstants;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.domain.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ResultTools {

	private static Logger logger = LoggerFactory.getLogger(ResultTools.class);
	/**
	 * 返回
	 * @return
	 */
	public static String setResponse(String code, String msg){
		
		Response response =new Response();
		response.setCode(code);
		response.setMsg(msg);
		return DataChangeTools.bean2gson(response);	
	}
	/**
	 * 返回
	 * @return
	 */
	public static String setNullMsgResponse(String code){
		
		Response response =new Response();
		response.setCode(code);
		return DataChangeTools.bean2gson(response);	
	}
	/**
	 * 
	 * @param code 
	 * @param data
	 * @return
	 */
	public static <T> String setResponse(String code,String msg, T data){
		
		ObjectResponse response =new ObjectResponse(code, msg, data);
		return DataChangeTools.bean2gson(response);
	}

    /**
     * 将响应结果中的data部分转换成指定类型后返回
     * @param responseResult
     * @param clazz
     * @return
     */
	public static ObjectResponse getObjectResponse(String responseResult, Class clazz) {
        ObjectResponse objectResponse = getObjectResponse(responseResult);
        if (objectResponse == null)
            return null;
        Object data = objectResponse.getData();
        if (data != null){
            String dataJson = DataChangeTools.bean2gson(data);
            logger.info("***data结果为: {}", dataJson);
            objectResponse.setData(DataChangeTools.gson2bean(dataJson, clazz));
        }
        return objectResponse;
    }
    /**
     * 将响应结果中的data列表部分转换成指定类型后返回
     * @param responseResult
     * @param cla
     * @return
     */
    @SuppressWarnings("rawtypes")
	public static <T> ObjectResponse getObjectListResponse(String responseResult, Class<T> cla) {
        ObjectResponse objectResponse = getObjectResponse(responseResult);
        if (objectResponse == null)
            return null;
        Object data = objectResponse.getData();
        if (data != null){
            String dataJson = DataChangeTools.bean2gson(data);
            logger.info("***data结果为: {}", dataJson);
            objectResponse.setData(DataChangeTools.gson2List(dataJson, cla));
        }
        return objectResponse;
    }

    /**
     * 判断响应的json结果，code码是否成功
     * @param responseBody
     * @return
     */
    public static boolean isSuccess(String responseBody){
        ObjectResponse objectResponse = getObjectResponse(responseBody);
        if (objectResponse != null && CodeConstants.SUCCESS.equals(objectResponse.getCode())){
            return true;
        }
        logger.info("***响应结果：{}",responseBody );
        return false;
    }
	/**
	 * 将响应结果转换为ObjectResponse对象
	 * @param responseBody
	 * @return
     */
	private static ObjectResponse getObjectResponse(String responseBody){
		if (StringUtils.isEmpty(responseBody)) return null;
		try {
			ObjectResponse response = DataChangeTools.gson2bean(responseBody, ObjectResponse.class);
			if (response != null) {
				return response;
			} else {
				logger.info("***responseBody不能转换为ObjectResponse对象，{}", responseBody);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("***转换为ObjectResponse对象异常，参数:{}，error:{}", responseBody, e.getMessage());
		}
		return null;
	}
}
