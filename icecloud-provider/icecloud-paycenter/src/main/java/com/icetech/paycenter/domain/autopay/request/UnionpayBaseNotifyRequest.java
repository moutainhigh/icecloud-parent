package com.icetech.paycenter.domain.autopay.request;

import com.icetech.common.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 *场内签约推送
 */
@Setter
@Getter
@ToString
public class UnionpayBaseNotifyRequest implements Serializable {

    @NotNull
    private String TransId;
    @NotNull
    private String MerCode;
    private String Remark;
    @NotNull
    private String Sign;

}
