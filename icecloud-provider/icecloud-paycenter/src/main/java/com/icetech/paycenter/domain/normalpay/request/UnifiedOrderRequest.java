package com.icetech.paycenter.domain.normalpay.request;


import com.icetech.common.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 统一下单接口请求
 */
@Getter
@Setter
@ToString
public class UnifiedOrderRequest implements Serializable {

    /**
     * 停车场编号，由支付中心统一分配
     */
    @NotNull
    private String parkCode;

    /**
     * 交易流水号
     */
    @NotNull
    private String tradeNo;

    /**
     * 下单交易类型
     */
    @NotNull
    private String selectTradeType;

    /**
     * 商品名称
     */
    @NotNull
    private String productInfo;
    /**
     *支付金额，单位为分
     */
    @NotNull
    private String price;

    /**
     *支付结果主动通知地址
     */
    @Deprecated
    private String notifyUrl;

    /**
     *微信支付授权码，微信公众号支付时必传
     */
    @Deprecated
    private String wxCode;

    /**
     *支付宝支付授权码，支付宝服务窗下必传PayCenter4WxServiceImpl
     */
    @Deprecated
    private String aliCode;

    /**
     * 微信下用户唯一标识
     */
    private String openId;

    /**
     * 支付宝下用户的唯一标识
     */
    private String userId;
    /**
     * 付款码
     */
    @NotNull(condition = "selectTradeType == *SCAN")
    private String payCode;

    /**
     * 设备信息
     */
    private String deviceInfo;

    /**
     * 下单与支付 ip 必须保持一致
     */
    private String spbillCreateIp;

    /**
     * 微信H5 支付时的场景上报信息，H5支付为必填
     */
    private  String sceneInfo;

    /**
     * 额外信息 (异步通知的时候会全部返回)
     */
    private String extraInfo;
}
