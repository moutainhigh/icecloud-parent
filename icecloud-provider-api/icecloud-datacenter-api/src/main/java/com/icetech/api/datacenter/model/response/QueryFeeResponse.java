package com.icetech.api.datacenter.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class QueryFeeResponse implements Serializable {

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

}
