package com.icetech.common;


import com.icetech.common.exception.ResponseBodyException;

public class AssertTools {

    /**
     * 对象不能为空，否则抛出自定义异常
     * @param object
     * @param errCode 错误码
     * @param message
     */
    public static void notNull(Object object, String errCode, String message) {
        if (object == null) {
            throw new ResponseBodyException(errCode, message);
        }
    }
}
