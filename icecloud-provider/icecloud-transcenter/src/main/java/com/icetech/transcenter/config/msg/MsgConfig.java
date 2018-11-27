package com.icetech.transcenter.config.msg;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 短信平台配置
 */
@ConfigurationProperties(prefix = "transcenter.msg")
@Data
@Component
public class MsgConfig {
    /**
     * 账户名称
     */
    private String operId;
    /**
     * 账户密码
     */
    private String operPass;

    /**
     * 请求地址
     */
    private String url;
    /**
     * 批量请求地址
     */
    private String batchUrl;

    /**
     * 附加号码
     */
    private String appendNumber;
}
