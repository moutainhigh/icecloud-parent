package com.icetech.datacenter.domain.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 车场基本响应参数
 * @author fangct
 */
@Getter
@Setter
@ToString
public class DiscountInfoResponse {

    private String discountNo;
    private Integer discountType;
    private String discountNumber;
    private Long discountTime;

}
