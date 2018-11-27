package com.icetech.paycenter.domain.response;

import lombok.Data;

import java.io.Serializable;

/**
 * 异步通知返回的信息类
 */
@Data
public class NotificationResponse implements Serializable {
    /**
     *
     */
    private String bankTradeNo;
    /**
     * 商户号
     */
    private String merchantNo;
    /**
     * 商户订单号
     */
    private String orderNo;
    /**
     * 平台号
     */
    private String platformId;
    /**
     * 收单流水号
     */
    private String serialNo;
    /**
     * 交易结果
     */
    private String tradeStatus;
    /**
     * 交易金额
     */
    private String amount;
    /**
     * 批次号
     */
    private String batchNo;
    /**
     * 发卡行银行行名称
     */
    private String cardName;
    /**
     *卡号 前六后四中间*
     */
    private String cardNo;
    /**
     * 卡类型
     */
    private String cardType;
    /**
     *发卡行行号
     */
    private String cbCode;
    /**
     * 银联终端号
     */
    private String cupTermId;
    /**
     * 设备序列号
     */
    private String cupTsamNo;
    /**
     * 交易手续费a'c'c
     */
    private String fee;
    /**
     * 参考号
     */
    private String refNo;
    /**
     * 保留域
     */
    private String reserve;
    /**
     * 凭证号
     */
    private String voucherNo;
    /**
     * 微信/支付宝订单号
     */
    private String centerSeqId;
    /**
     * others info
     */
    private String centerInfo;
    /**
     * 收单到微信/支付宝下单编号
     */
    private String bankOrderNo;
}
