package com.icetech.paycenter.domain.normalpay.response;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * 统一下单接口响应
 */
@Getter
@Setter
public class UnifiedOrderResponse implements Serializable {

    /**
     * 支付要素 民生银行
     */
    private String payInfo;
    /**
     * Map 类型的支付要素
     */
    private Map mapPayInfo;
    /**
     * 支付状态
     */
    private String tradeStatus;
}
