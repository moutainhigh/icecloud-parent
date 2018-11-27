package com.icetech.transcenter.domain.request;

import com.icetech.common.annotation.NotNull;
import com.icetech.transcenter.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;

/**
 * api 模块 请求
 * @author wangzw
 */
@Getter
@Setter
public class SmsRequest extends BaseDomain {
    /**
     * 发送方的手机号码
     */
    @NotNull
    private String desMobile;
    /**
     * 发送内容(最长不要超过500个字)
     */
    @NotNull
    private String content;
    /**
     * 有效时间(单位为秒)
     */
    private Integer times;
}
