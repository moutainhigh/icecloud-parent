package com.icetech.paycenter.domain.normalpay.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class CmbcCommonRequest implements Serializable {

    /**
     * 请求内容
     */
    private String businessContext;

    /**
     *
     */
    private String merchantNo;

    /**
     *用于版本控制、灰度发布、向下兼容等用途
     */
    private String version ;

    /**
     * 商户流水
     */
    private String merchantSeq;
    /**
     *交易日期
     */
    private String transDate;
    /**
     *交易时间
     */
    private String transTime;
    /**
     *交易编码
     */
    private String  transCode;
    /**
     * 安全类型
     */
    private String securityType;
    /**
     *
     */
    private String sessionId;
    private String source;

    /**
     *备用字段
     */
    private String reserve1;
    /**
     *备用字段
     */
    private String reserve2;
    /**
     *备用字段
     */
    private String reserve3;
    /**
     *备用字段
     */
    private String reserve4;
    /**
     *备用字段
     */
    private String reserve5;

    /**
     *Json 格式的通用备用字段
     */
    private String reserveJson;

}
