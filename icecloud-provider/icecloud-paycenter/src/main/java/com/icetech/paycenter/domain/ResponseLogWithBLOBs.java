package com.icetech.paycenter.domain;

/**
 * 
 */

public class ResponseLogWithBLOBs extends ResponseLog {
    /**
     * 请求参数
     */
    private String reqParams;

    /**
     * 返回结果
     */
    private String returnResult;

    /**
     * 请求参数
     * @return req_params 请求参数
     */
    public String getReqParams() {
        return reqParams;
    }

    /**
     * 请求参数
     * @param reqParams 请求参数
     */
    public void setReqParams(String reqParams) {
        this.reqParams = reqParams == null ? null : reqParams.trim();
    }

    /**
     * 返回结果
     * @return return_result 返回结果
     */
    public String getReturnResult() {
        return returnResult;
    }

    /**
     * 返回结果
     * @param returnResult 返回结果
     */
    public void setReturnResult(String returnResult) {
        this.returnResult = returnResult == null ? null : returnResult.trim();
    }
}