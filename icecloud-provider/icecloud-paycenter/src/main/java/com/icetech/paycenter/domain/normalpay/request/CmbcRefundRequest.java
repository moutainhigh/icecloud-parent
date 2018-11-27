package com.icetech.paycenter.domain.normalpay.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 民生银行退款请求
 * @author wangzw
 */
@Data
public class CmbcRefundRequest implements Serializable {
    /**
     * 停车厂Id
     */
    private String platformId;
    /**
     *民生统一商户号
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
     * 退款金额
     */
    private String orderAmount;
    /**
     * 退款说明
     */
    private String orderNote;
    /**
     * 备注字段
     */
    private String reserve;
}
