package com.icetech.datacenter.domain.request;

import com.icetech.common.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AutopayRequest {
    @NotNull
    private String orderNum;
    @NotNull
    private String plateNum;
    @NotNull
    private String totalPrice;
    @NotNull
    private String paidPrice;
    @NotNull
    private String discountPrice;
    @NotNull
    private Integer parkTime;
    @NotNull
    private String unpayPrice;
    @NotNull
    private Long payTime;
}
