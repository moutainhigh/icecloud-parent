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
public class AutopayResponse {

    private String tradeNo;
    private Integer payWay;

}
