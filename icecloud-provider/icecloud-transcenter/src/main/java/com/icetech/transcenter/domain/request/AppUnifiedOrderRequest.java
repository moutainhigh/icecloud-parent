package com.icetech.transcenter.domain.request;

import com.icetech.common.annotation.NotNull;
import com.icetech.transcenter.domain.BaseDomain;
import lombok.Data;

/**
 * app 下单请求
 * @author wangzw
 */
@Data
public class AppUnifiedOrderRequest extends BaseDomain {

    /**
     * 车厂编号
     */
    @NotNull
    private String parkCode;
    /**
     * 交易流水号
     */
    @NotNull
    private String tradeNo;
    /**
     * 交易类型
     */
    @NotNull
    private String tradeType;
    /**
     * 商品名称
     */
    @NotNull
    private String productInfo;
    /**
     * 支付金额（单位：分）
     */
    @NotNull
    private String price;
}
