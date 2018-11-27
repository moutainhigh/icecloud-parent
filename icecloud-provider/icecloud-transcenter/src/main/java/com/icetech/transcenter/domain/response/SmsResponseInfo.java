package com.icetech.transcenter.domain.response;

import com.icetech.transcenter.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;

/**
 * 短信响应
 * @author wangzw
 */
@Getter
@Setter
public class SmsResponseInfo extends BaseDomain {
    private String desmobile;
    private String msgid;
}
