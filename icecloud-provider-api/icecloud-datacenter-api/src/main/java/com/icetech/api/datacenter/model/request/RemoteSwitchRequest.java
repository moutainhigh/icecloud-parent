package com.icetech.api.datacenter.model.request;

import com.icetech.common.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 远程开关闸请求参数
 * @author fangct
 */
@Setter
@Getter
@ToString
public class RemoteSwitchRequest implements Serializable {
    @NotNull
    private String parkCode;
    @NotNull
    private String deviceNo;
    @NotNull
    private String channelId;
    @NotNull
    private Integer switchType;
    @NotNull
    private String sequenceId;
    private String operAccount;

}
