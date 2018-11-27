package com.icetech.transcenter.domain.request;

import com.icetech.common.annotation.NotNull;
import com.icetech.transcenter.domain.BaseDomain;
import lombok.Data;

/**
 * app 下支付结果查询请求
 * @author wangzw
 */
@Data
public class AppPayResultRequest extends BaseDomain {
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
     * 支付类型
     */
    @NotNull
    private String tradeType;
}
