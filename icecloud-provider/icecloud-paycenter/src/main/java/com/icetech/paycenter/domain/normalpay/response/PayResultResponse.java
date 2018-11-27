package com.icetech.paycenter.domain.normalpay.response;

import lombok.Data;

import java.io.Serializable;

/**
 * 支付结果查询响应
 * @author wangzw
 */
@Data
public class PayResultResponse implements Serializable {
    private String price;
    private String tradeStatus;
    private String tradeType;
}
