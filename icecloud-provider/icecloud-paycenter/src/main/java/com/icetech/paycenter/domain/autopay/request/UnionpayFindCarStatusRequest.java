package com.icetech.paycenter.domain.autopay.request;

import com.icetech.common.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *银联无感支付-场内状态查询
 */
@Setter
@Getter
@ToString
public class UnionpayFindCarStatusRequest extends UnionpayBaseNotifyRequest {

    @NotNull
    private String ParkCode;
//    @NotNull
    private String ParkName;
    @NotNull
    private String CarNo;
    @NotNull
    private String RefNo;
    @NotNull
    private String EntryTime;

}
