package com.icetech.datacenter.domain.request;

import com.icetech.common.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class DiscountInfoRequest {
    @NotNull
    private String orderNum;
}
