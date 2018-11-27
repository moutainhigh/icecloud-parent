package com.icetech.paycenter.domain.autopay.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 银联免密支付-车辆状态查询
 * @author fangct
 */
@Setter
@Getter
public class UnionpayFindCarStatusResponse extends UnionpayResponse {

    /**
     * 车辆状态
     */
    private Integer status;

}
