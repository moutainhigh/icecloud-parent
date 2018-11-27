package com.icetech.datacenter.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotifyPrepayRequest {
    private String orderNum;
    private String plateNum;
    private String channelId;
    private String tradeNo;
    private String totalPrice;
    private String prepay;
    private String discountPrice;
    private String discountNos;
    private Integer payWay;
    private Integer payChannel;
    private String payTerminal;
    private Long payTime;

}
