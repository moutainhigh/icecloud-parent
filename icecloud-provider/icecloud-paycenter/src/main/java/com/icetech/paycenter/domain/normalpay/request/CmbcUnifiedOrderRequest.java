/**
 * Copyright 2018 bejson.com
 */
package com.icetech.paycenter.domain.normalpay.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 民生银行下单请求
 *
 * @author wangzw
 */
@Getter
@Setter
@ToString
public class CmbcUnifiedOrderRequest implements Serializable {

    /**
     * 接入平台号
     */
    private String platformId;

    /**
     * 民生商户号
     */
    private String merchantNo;

    /**
     * 商户订单号
     */
    private String merchantSeq;

    /**
     * 商户流水号
     */
    private String mchSeqNo;

    /**
     * 支付类型
     */
    private String selectTradeType;

    /**
     * 交易金额
     */
    private String amount;

    /**
     * 订单详情
     */
    private String orderInfo;

    /**
     * 通知地址
     */
    private String notifyUrl;

    /**
     * 反扫时为付款码
     */
    private String remark;

    /**
     * 订单日期 |(格式：yyyyMMdd)
     */
    private String transDate;

    /**
     * 订单时间 (格式：yyyyMMddHHmmssSSS)
     */
    private String transTime;

    /**
     * 请求扩展字段
     */
    private String inExtData;

    /**
     * 设备信息
     */
    private String deviceInfo;

    /**
     *微信公众号支付 API 下，该部分必输，填子公众 号的 appId
     */
    private String subAppId;

    /**
     *微信公众号支付 API 下，该部分必输，填用户的 openId
     */
    private String subOpenId;

    /**
     * 支付宝服务窗下，该字段必输
     */
    private String userId;

    /**
     * 下单与支付 ip 微信app跳转支付必填
     */
    private String spbillCreateIp;
}