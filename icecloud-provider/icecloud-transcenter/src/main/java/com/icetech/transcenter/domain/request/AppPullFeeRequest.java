package com.icetech.transcenter.domain.request;

import com.icetech.common.annotation.NotNull;
import com.icetech.transcenter.domain.BaseDomain;
import lombok.Data;

/**
 * app 拉取费用请求
 * @author wangzw
 */
@Data
public class AppPullFeeRequest extends BaseDomain {
    /**
     * 云平台订单号
     */
    @NotNull
    private String orderNum;
}
