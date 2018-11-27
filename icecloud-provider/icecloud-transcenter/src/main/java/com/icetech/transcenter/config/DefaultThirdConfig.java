package com.icetech.transcenter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 默认第三方配置
 * @author wangzw
 */
@ConfigurationProperties(prefix = "transcenter.third.config")
@Data
@Component
public class DefaultThirdConfig {
    /**
     * 平台唯一号
     */
    private String pid;
    /**
     * 平台密码
     */
    private String key;
    /**
     * 车场编号
     */
    private String parkCode;
}
