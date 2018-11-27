package com.icetech.paycenter.domain;

import lombok.Data;

import java.util.Date;

/**
 * 
 */
@Data
public class ParkWeixin {
    /**
     * id (主健自增)
     */
    private Integer id;

    /**
     * 车场id
     */
    private String parkCode;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * APIKEY
     */
    private String apiKey;

    /**
     * 商户ID
     */
    private String mchId;

    /**
     * 开发者秘钥
     */
    private String appSecret;

    /**
     * 退款异步通知地址
     */
    private String refundNotifyUrl;
    /**
     * 支付异步通知地址
     */
    private String notifyUrl;
    /**
     * 状态1启用2不启用默认1
     */
    private Integer status;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新时间
     */
    private String updateUser;

    /**
     * 证书存放地址
     */
    private String certPath;

}