package com.icetech.api.paycenter.model.request;

import com.icetech.common.annotation.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class ExitpayRequestDto implements Serializable {

    @NotNull
    private String parkCode;
    @NotNull
    private String tradeNo;
    @NotNull
    private String plateNum;
    @NotNull
    private String enterTime;
    @NotNull
    private String exitTime;
    @NotNull
    private String totalPrice;
    @NotNull
    private String unpayPrice;

}
