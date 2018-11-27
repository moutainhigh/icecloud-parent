package com.icetech.api.paycenter.model.request;

import com.icetech.common.annotation.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class PayResultRequestDto implements Serializable {
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
