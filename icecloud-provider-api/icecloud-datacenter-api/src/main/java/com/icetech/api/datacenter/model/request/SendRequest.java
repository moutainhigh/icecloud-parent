package com.icetech.api.datacenter.model.request;

import com.icetech.common.annotation.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 下发请求
 * @author fangct
 */
@Data
public class SendRequest implements Serializable {
    /**
     * 车场id
     */
    @NotNull
    private Long parkId;
    /**
     * 业务类型
     */
    @NotNull
    private Integer serviceType;
    /**
     * 业务数据ID
     */
    @NotNull
    private Long serviceId;
}
