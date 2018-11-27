package com.icetech.transcenter.domain.response;

import com.icetech.transcenter.domain.BaseDomain;
import lombok.Data;

import java.util.Map;

/**
 * app 下单响应
 * @author wangzw
 */
@Data
public class AppUnifiedOrderResponse extends BaseDomain {

    private Map payInfo;
}
