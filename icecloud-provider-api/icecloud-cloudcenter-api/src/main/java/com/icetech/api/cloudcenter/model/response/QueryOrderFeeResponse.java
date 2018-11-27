package com.icetech.api.cloudcenter.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
public class QueryOrderFeeResponse implements Serializable {

    private String orderNum;
    private String plateNum;
    private String totalAmount;
    private String paidAmount;
    private String discountAmount;
    private String discountPrice;
    private String unpayPrice;
    private Long parkTime;
    private Long payTime;
    private Long queryTime;

    private Integer status;
    private Long enterTime;
    private String parkName;

}
