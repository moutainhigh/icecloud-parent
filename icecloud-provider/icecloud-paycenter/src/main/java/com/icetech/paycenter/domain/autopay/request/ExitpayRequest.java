package com.icetech.paycenter.domain.autopay.request;

import com.icetech.common.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 离场扣费
 * @author fangct
 */
@Setter
@Getter
@ToString
public class ExitpayRequest implements Serializable {

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
