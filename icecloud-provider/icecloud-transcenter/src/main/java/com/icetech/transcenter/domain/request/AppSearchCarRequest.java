package com.icetech.transcenter.domain.request;

import com.icetech.common.annotation.NotNull;
import com.icetech.transcenter.domain.BaseDomain;
import lombok.Data;

/**
 * app 端查询车辆实体
 * @author wangzw
 */
@Data
public class AppSearchCarRequest extends BaseDomain {
    /**
     * 车场编号
     */
    @NotNull
    private String parkCode;
    /**
     * 车牌号 (模糊匹配)
     */
    @NotNull
    private String plateNum;
    /**
     * 第几页
     */
    private Integer page=1;
    /**
     * 分页大小
     */
    private Integer pageSize=10;
}
