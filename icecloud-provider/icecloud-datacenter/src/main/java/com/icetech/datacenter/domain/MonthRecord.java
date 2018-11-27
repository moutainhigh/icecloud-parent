package com.icetech.datacenter.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class MonthRecord {
    private Long id;

    private Long monthId;

    private Long productId;

    private Long parkId;

    private Integer cardProperty;

    private String cardOwner;

    private String plateNum;

    private String spaceNum;

    private String phone;

    private Integer cardOpertype;

    private Integer isOverdue;

    private Integer plotCount;

    private Integer count;

    private String payMoney;

    private Integer payMethod;

    private String refund;

    private String startTime;

    private String endTime;

    private String operAccount;

    private String remark;

    private String createTime;

    private String updateTime;

}