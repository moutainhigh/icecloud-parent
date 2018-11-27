package com.icetech.paycenter.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 银联无感支付配置中心
 */
@ConfigurationProperties(prefix = "paycenter.unionpay")
@Component
@Setter
@Getter
public class UnionpayConfig {

    /**
     * 请求地址
     */
    private String url;

    /**
     * FTP请求地址
     */
    private String ftpUrl;
    /**
     * FTP端口
     */
    private String ftpPort;

    /**
     * FTP用户名
     */
    private String ftpUsername;

    /**
     * FTP密码
     */
    private String ftpPassword;

}
