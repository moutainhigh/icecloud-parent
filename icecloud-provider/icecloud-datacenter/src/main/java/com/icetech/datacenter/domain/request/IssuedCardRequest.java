package com.icetech.datacenter.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IssuedCardRequest {

    private String prodName;
    private Integer prodDuration;
    private String cardId;
    private String plateNum;
    private String phone;
    private String cardOwner;
    private Integer cardProperty;
    private String startDate;
    private String endDate;
    private Integer cardType;
    private String startTime;
    private String endTime;
    private Integer cardOperType;
    private Integer plotCount	;
    private String plotNum	;
    private String payMoney;
    private String operAccount;
    private Long operTime;

}
