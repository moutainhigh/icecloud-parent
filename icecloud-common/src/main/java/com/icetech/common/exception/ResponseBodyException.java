package com.icetech.common.exception;

import java.io.Serializable;

/**
 * description: 返回ResponseBody时出现异常
 * @author fangct
 * @since 1.0
 */
public class ResponseBodyException extends RuntimeException
        implements Serializable {

    private String errCode;

    public ResponseBodyException(String errCode, String message) {
        super(message);
        this.errCode = errCode;
    }

    public String getMessage() {
        return super.getMessage();
    }

    public String getErrCode() {
        return this.errCode;
    }

    public String toString() {
        return super.toString();
    }
}
