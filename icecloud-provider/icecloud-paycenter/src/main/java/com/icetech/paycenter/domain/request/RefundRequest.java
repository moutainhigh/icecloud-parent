package com.icetech.paycenter.domain.request;

import com.icetech.common.annotation.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 发起退款请求
 * @author wangzw
 */
@Data
public class RefundRequest implements Serializable {
    /**
     * 停车场编号
     */
    @NotNull
    private String parkCode;
    /**
     * 交易流水号
     */
    @NotNull
    private String tradeNo;

    /**
     * 退款流水号
     */
    @NotNull
    private String refundTradeNo;
    /**
     * 价格
     */
    @NotNull
    private String price;
    /**
     * 退款说明
     */
    @NotNull
    private String orderNote;

}
