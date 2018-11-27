package com.icetech.paycenter.domain.autopay.request;

import com.icetech.common.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
public class EnterNotifyRequest implements Serializable {

    @NotNull
    private String parkCode;
    @NotNull
    private String orderNum;
    @NotNull
    private String plateNum;
    @NotNull
    private String enterTime;

}
