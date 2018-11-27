package com.icetech.paycenter.domain.request;

import com.icetech.common.annotation.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 关闭订单实体类
 * @author wangzw
 */
@Data
public class CloseOrderRequest implements Serializable {
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
}
