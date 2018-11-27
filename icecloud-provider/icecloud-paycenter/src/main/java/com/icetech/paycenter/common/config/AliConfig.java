package com.icetech.paycenter.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 支付宝支付配置
 * @author wangzw
 */

@ConfigurationProperties(prefix = "paycenter.ali")
@Component
@Data
public class AliConfig {
    public static String URL="https://openapi.alipay.com/gateway.do";
    public static String DEV_URL="https://openapi.alipaydev.com/gateway.do";
    /**
     * 编码
     */
    public static String CHARSET = "UTF-8";
    /**
     * 返回格式
     */
    public static String FORMAT = "json";
    /**
     * RSA2
     */
    public static String SIGNTYPE = "RSA2";
    /**
     * 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
     */
    private String baseNotifyUrl;
    /**
     * 是否沙箱环境
     */
    private Boolean isSandbox;


}
