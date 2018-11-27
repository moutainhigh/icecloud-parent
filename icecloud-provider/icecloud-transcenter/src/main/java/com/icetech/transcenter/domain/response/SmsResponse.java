package com.icetech.transcenter.domain.response;

import com.icetech.transcenter.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * api 模块响应
 * @author wangzw
 */
@Getter
@Setter
public class SmsResponse extends BaseDomain {
    /**
     * 返回码
     */
    private String code;
    /**
     * 短信Id
     */
    private List<SmsResponseInfo> message;

}