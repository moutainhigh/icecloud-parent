package com.icetech.cloudcenter.domain.order.update;

import com.icetech.common.domain.OrderInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class OrderInfoUpdate {
    /**
     * where条件的参数
     */
    private OrderInfo old;
    /**
     * update更新的参数
     */
    private OrderInfo neo;

}
