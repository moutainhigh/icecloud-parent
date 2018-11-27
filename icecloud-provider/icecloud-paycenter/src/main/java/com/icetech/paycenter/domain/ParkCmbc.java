package com.icetech.paycenter.domain;

import lombok.Data;

import java.util.Date;

/**
 * 民生聚合支付
 * @author wangzw
 */
@Data
public class ParkCmbc {
    /**
     * 主键自增长id
     */
    private Integer id;

    /**
     * 车场id
     */
    private String parkCode;

    /**
     * 民生银行为接入平台分配的平台编号
     */
    private String platformId;

    /**
     * 民生统一商户号
     */
    private String merchantNo;

    /**
     * 民生商户私钥
     */
    private String privateKey;
    /**
     * 商户私钥路径
     */
    private String merchantPrivateKeyPath;
    /**
     * 民生银行公钥路径
     */
    private String cmbcPublicKeyPath;
    /**
     * 异步通知地址
     */
    private String notifyUrl;

    /**
     * 微信appid
     */
    private String wxAppId;
    /**
     * 微信开发者密匙
     */
    private String wxAppSecret;
    /**
     * 微信支付目录
     */
    private String wxPayPath;
    /**
     * 支付宝应用id
     */
    private String aliAppId;
    /**
     * 支付宝商户私钥
     */
    private String aliPrivateKey;
    /**
     * 支付宝公钥
     */
    private String aliPublicKey;
    /**
     * 支付渠道  0=未选取(默认); 1=微信; 2=支付宝; 3=微信和支付宝
     */
    private String payChannel;
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

}