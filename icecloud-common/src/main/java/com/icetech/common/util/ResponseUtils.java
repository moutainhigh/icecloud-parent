package com.icetech.common.util;

import com.icetech.common.constants.CodeConstants;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.exception.ResponseBodyException;

public class ResponseUtils {

    public static <T> ObjectResponse<T> returnSuccessResponse(){
        return new ObjectResponse<T>(CodeConstants.SUCCESS, CodeConstants.getName(CodeConstants.SUCCESS));
    }

    /**
     * 返回正确的结果
     * @param t
     * @param <T>
     * @return
     */
    public static <T> ObjectResponse<T> returnSuccessResponse(T t){
        if (t == null){
            return new ObjectResponse<T>(CodeConstants.SUCCESS, CodeConstants.getName(CodeConstants.SUCCESS));
        }else{
            return new ObjectResponse<T>(CodeConstants.SUCCESS, CodeConstants.getName(CodeConstants.SUCCESS), t);
        }
    }

    /**
     * 返回异常的结果
     * @param errorCode
     * @return
     */
    public static ObjectResponse returnErrorResponse(String errorCode){
        return new ObjectResponse(errorCode, CodeConstants.getName(errorCode));
    }

    /**
     * 返回异常的结果
     * @param errorCode
     * @param errorMsg
     * @return
     */
    public static ObjectResponse returnErrorResponse(String errorCode, String errorMsg){
        return new ObjectResponse(errorCode, CodeConstants.getName(errorCode) + "," + errorMsg);
    }

    /**
     * 返回结果不能失败，否则抛出异常
     * @param objectResponse
     */
    public static void notError(ObjectResponse objectResponse){
        if (objectResponse == null){
            throw new ResponseBodyException(CodeConstants.ERROR, CodeConstants.getName(CodeConstants.ERROR));
        }
        if (!objectResponse.getCode().equals(CodeConstants.SUCCESS)){
            throw new ResponseBodyException(objectResponse.getCode(), objectResponse.getMsg());
        }
    }
}
