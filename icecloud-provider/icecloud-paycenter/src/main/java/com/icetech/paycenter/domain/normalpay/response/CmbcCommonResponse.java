package com.icetech.paycenter.domain.normalpay.response;

import lombok.Data;

import java.io.Serializable;

/**
 * 民生银行下单公共返回
 * @author wangzw
 */
@Data
public class CmbcCommonResponse implements Serializable {
    /**
     * 响应内容
     */
    private String businessContext;
    private String gateReturnCode;
    private String gateReturnMessage;
    private String gateReturnType;
    private String gateSeq;
    private String gateTransDate;
    private String gateTransTime;
    private String merchantSeq;
    private String reserve1;
    private String reserve2;
    private String reserve3;
    private String reserveJson;
    private String transCode;
}
