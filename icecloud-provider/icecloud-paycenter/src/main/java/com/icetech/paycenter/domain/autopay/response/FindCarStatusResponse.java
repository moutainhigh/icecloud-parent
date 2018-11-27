package com.icetech.paycenter.domain.autopay.response;

import com.icetech.common.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 *场内状态查询返回
 */
@Setter
@Getter
@ToString
public class FindCarStatusResponse implements Serializable {

    @NotNull
    private Integer status;
    private String exitTime;

}
