package com.icetech.api.paycenter.model.request;

import com.icetech.common.annotation.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class ExitNotifyRequestDto implements Serializable {


    @NotNull
    private String parkCode;
    @NotNull
    private String orderNum;
    @NotNull
    private String plateNum;
    @NotNull
    private String exitTime;
    @NotNull
    private String paidPrice;
}
