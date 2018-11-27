package com.icetech.api.datacenter.model.request;

import com.icetech.common.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 无牌车入场请求参数
 * @author fangct
 */
@Setter
@Getter
@ToString
public class NoplateEnterRequest implements Serializable {
    @NotNull
    private String parkCode;
    @NotNull
    private String channelId;
    @NotNull
    private String plateNum;
    @NotNull
    private Long enterTime;

}
