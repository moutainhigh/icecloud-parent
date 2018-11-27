package com.icetech.api.datacenter.model.request;

import com.icetech.common.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
public class PrepayReportRequest implements Serializable {
    private Long parkId;
    private String parkCode;
    @NotNull
    private String orderNum;
    @NotNull
    private String plateNum;
    @NotNull
    private String tradeNo;
    @NotNull
    private String totalPrice;
    @NotNull
    private String paidPrice;
    @NotNull
    private String discountPrice;
    private String discountNos;
    @NotNull
    private Integer payWay;
    @NotNull
    private Integer payChannel;
    private String payTerminal;
    private String userAccount;
    @NotNull
    private Long payTime;

}
