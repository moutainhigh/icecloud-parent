package com.icetech.api.paycenter.model.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class PayResultResponseDto implements Serializable {
    private String price;
    private String tradeStatus;
    private String tradeType;
}
