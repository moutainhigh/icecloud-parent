package com.icetech.transcenter.domain.request;

import com.icetech.common.annotation.NotNull;
import com.icetech.transcenter.domain.BaseDomain;
import lombok.Data;

/**
 * 当车主通过app交费完成后，预缴费结果通知
 * @author wangzw
 */
@Data
public class AppPrePaymentNotifyRequest extends BaseDomain {
    /**
     * 云平台订单号
     */
    @NotNull
    private String orderNum;
    /**
     * 车牌号
     */
    @NotNull
    private String plateNum;
    /**
     * 交易流水号
     */
    @NotNull
    private String tradeNo;
    /**
     * 应收金额
     */
    @NotNull
    private String totalPrice;
    /**
     * 预付金额，本次实收金额
     */
    @NotNull
    private String prepay;
    /**
     * 优惠金额，本次优惠金额
     */
    @NotNull
    private String discountPrice;
    /**
     * 本次预缴费使用的电子优惠券编号，当discount_price大于0时，必传，如果有多个，中间用逗号隔开
     */
    private String discountNos;
    /**
     * 缴费方式
     */
    @NotNull
    private Integer payWay;
    /**
     * 缴费渠道，详情查看数据定义
     */
    @NotNull
    private Integer payChannel;
    /**
     * 缴费终端，详情查看数据定义
     */
    private String payTerminal;
    /**
     * 支付时间（unix时间戳）
     */
    @NotNull
    private Long payTime;

}
