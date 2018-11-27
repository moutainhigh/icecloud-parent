package com.icetech.paycenter.domain;

import lombok.Data;

import java.util.Date;

/**
 * 
 */
@Data
public class AccountRecord {
    /**
     * 主健自增长
     */
    private Integer id;

    /**
     * 交易日期
     */
    private Date tradeDate;

    /**
     * 支付平台类型
     */
    private String tradeType;

    /**
     * 支付场景
     */
    private Integer payScene;

    /**
     * 进账金额（单位：分）
     */
    private Integer income;

    /**
     * 外部应用交易号
     */
    private String tradeNo;

    /**
     * 停车场ID，本平台id
     */
    private String parkCode;

    /**
     * 支付设备的标示：H5P：H5付，SSPM：自助缴费机，CDP：本地扫码枪
     */
    private String terminalType;

    /**
     * 下单的交易订单号(park_code+trade_no)
     */
    private String outTradeNo;

    /**
     * 微信的交易订单号或支付宝的交易流水号
     */
    private String centerSeqId;

    /**
     * 用户在商户appid下的唯一标识（微信支付下有效）
     */
    private String openId;
    /**
     * 支付宝用户的唯一标识
     */
    private String userId;

    /**
     * 支付的状态，PayCenterTradeStatus
     */
    private String status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 其他信息 第三方支付平台返回的其他支付信息
     */
    private String centerInfo;
}