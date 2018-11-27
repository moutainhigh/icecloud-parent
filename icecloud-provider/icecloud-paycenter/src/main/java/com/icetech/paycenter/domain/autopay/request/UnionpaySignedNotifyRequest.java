package com.icetech.paycenter.domain.autopay.request;

import com.icetech.common.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *场内签约推送
 */
@Setter
@Getter
@ToString
public class UnionpaySignedNotifyRequest extends UnionpayBaseNotifyRequest {

    @NotNull
    private String CarNo;
    @NotNull
    private String CustId;
    @NotNull
    private String OperaType;

}
