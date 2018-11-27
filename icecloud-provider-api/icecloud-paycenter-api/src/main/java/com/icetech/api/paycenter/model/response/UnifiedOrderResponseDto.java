package com.icetech.api.paycenter.model.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;
@Data
public class UnifiedOrderResponseDto implements Serializable {
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
