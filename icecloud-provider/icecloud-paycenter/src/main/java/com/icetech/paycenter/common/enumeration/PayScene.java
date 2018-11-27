package com.icetech.paycenter.common.enumeration;

import lombok.Getter;

/**
 * 接口响应的交易状态
 */
public enum PayScene {
    主动支付(1),
    免密支付(2),
    ;
    private @Getter Integer value;
    PayScene(Integer value){
        this.value = value;
    }
}
