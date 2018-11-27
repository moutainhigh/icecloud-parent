package com.icetech.api.paycenter.model.request;

import com.icetech.common.annotation.NotNull;
import lombok.Data;

@Data
public class PayCenterBaseRequestDto<T> {
    @NotNull
    private String pid;
    @NotNull
    public String serviceName;
    @NotNull
    public String sign;
    @NotNull
    public Long timestamp;
    @NotNull
    public T bizContent;
}
