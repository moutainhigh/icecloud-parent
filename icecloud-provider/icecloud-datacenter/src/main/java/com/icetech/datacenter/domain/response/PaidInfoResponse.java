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
public class PaidInfoResponse {

    private String tradeNo;
    private String totalPrice;
    private String paidPrice;
    private String discountPrice;
    private Integer payWay;
    private Integer payChannel;
    private String payTerminal;
    private String userAccount;
    private Long payTime;

}
