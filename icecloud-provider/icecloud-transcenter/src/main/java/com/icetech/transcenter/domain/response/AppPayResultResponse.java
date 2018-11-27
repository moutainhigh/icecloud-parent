package com.icetech.transcenter.domain.response;

import com.icetech.transcenter.domain.BaseDomain;
import lombok.Data;

/**
 * app 支付结果查询响应
 * @author wangzw
 */
@Data
public class AppPayResultResponse extends BaseDomain {
    /**
     * 交易金额
     */
    private String price;
    /**
     * 交易状态
     */
    private String tradeStatus;
}
