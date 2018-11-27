package com.icetech.paycenter.domain.normalpay.response;

import lombok.Data;

import java.io.Serializable;

/**
 * 民生银行查询响应
 * @author wangzw
 */
@Data
public class CmbcPayResultReponse implements Serializable {
    /**
     * 交易金额 单位为分
     */
    private String amount;
    /**
     * 收单到微信下单编号
     */
    private String bankOrderNo;
    /**
     * 收单系统流水号
     */
    private String bankTradeNo;
    /**
     * 批次号
     */
    private String batchNo;
    /**
     * 发卡行行名
     */
    private String cardName;
    /**
     * 卡号
     */
    private String cardNo;
    /**
     * 卡类型  0 借记卡 1 贷记卡
     */
    private String cardType;
    /**
     * 发卡行行号
     */
    private String cbCode;
    /**
     *其他信息
     */
    private String centerInfo;
    /**
     * 微信订单号
     */
    private String centerSeqId;
    /**
     * 银联终端号
     */
    private String cupTermId;
    /**
     * 设备序列号
     */
    private String cupTsamNo;
    /**
     * 交易手续费
     */
    private String fee;
    /**
     * 回显商户名
     */
    private String merchantName;
    /**
     * 商户订单号
     */
    private String merchantSeq;
    /**
     *订单详情
     */
    private String orderInfo;
    /**
     * 参考号
     */
    private String refNo;
    /**
     * 所查询订单的状态
     */
    private String remark;
    /**
     * S 订单交易成功 E 订单失败 R 原订单成功，未支付（待支付) C 已撤销（理论上不存在) 已关闭 T 订单转入退款
     */
    private String tradeStatus;
    /**
     * 交易类型
     */
    private String tradeType;
    /**
     * 凭证号
     */
    private String voucherNo;
}
