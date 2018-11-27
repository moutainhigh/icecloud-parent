package com.icetech.paycenter.domain.normalpay.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 民生银行支付结果查询请求
 * @author wangzw
 */
@Data
public class CmbcPayResultRequest implements Serializable {
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
     * 查询类型  1.支付 2.退款
     */
    private String tradeType;
    /**
     * 原交易凭证号 (当查询类型为退款时必须输入)
     */
    private String orgvoucherNo;
    /**
     * 备注字段
     */
    private String reserve;
}
