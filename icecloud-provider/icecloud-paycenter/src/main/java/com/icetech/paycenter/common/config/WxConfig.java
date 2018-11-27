package com.icetech.paycenter.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信支付配置
 * @author wangzw
 */
@ConfigurationProperties(prefix = "paycenter.wx")
@Data
@Component
public class WxConfig {
    /**
     * 微信下单异步通知的地址
     */
    private String baseUnifiedOrderNotifyUrl;

    /**
     * 微信退款异步通知的地址
     */
    private String baseRefundNotifyUrl;

    /**
     * 是否开启沙箱环境
     */
    private Boolean useSandboxEnv;
}
