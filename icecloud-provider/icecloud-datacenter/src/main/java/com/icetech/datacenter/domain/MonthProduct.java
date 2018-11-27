package com.icetech.datacenter.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class MonthProduct {
    private Integer id;

    private String name;

    private Integer parkId;

    private Integer duration;

    private Integer cardType;

    private String paymoney;

    private String startDate;

    private String endDate;

    private String startTime;

    private String endTime;

    private Integer checkStatus;

    private Integer buyChannel;

    private Integer isRenewFee;

    private Integer isUserSms;

    private Integer remainDay;

    private Integer isDiftDay;

    private Integer diftDays;

    private Integer status;

    private String feedback;

    private String adder;

    private String editer;

    private Integer delflag;

    private String createTime;
}