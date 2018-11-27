package com.icetech.paycenter.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 民生银行支付配置中心
 */
@ConfigurationProperties(prefix = "paycenter.cmbc")
@Data
@Component
public class CmbcConfig {

    /**测试环境地址**/
    /**
     * 下单地址 test env
     */
    public static String UNIFIED_ORDER_URL_DEV= "http://wxpay.cmbc.com.cn:1080/mobilePlatform/appserver/lcbpPay.do";
    /**
     * 支付结果查询地址
     */
    public static String PAY_RESULT_URL_DEV= "http://wxpay.cmbc.com.cn:1080/mobilePlatform/appserver/paymentResultSelect.do";
    /**
     * 退款通知地址
     */
    public static String REFUND_URL_DEV= "http://wxpay.cmbc.com.cn:1080/mobilePlatform/appserver/cancelTrans.do";
    /**
     * 下载明细地址
     */
    public static String DOWNLOAD_URL_DEV= "";

    /**正式环境地址**/
    public static String UNIFIED_ORDER_URL= "";
    public static String PAY_RESULT_URL= "";
    public static String REFUND_URL= "";
    public static String DOWNLOAD_URL= "";
    /**
     * 异步通知地址
     */
    private String baseNotifyUrl;

    /**
     * 交易单下载路径
     */
    private String filePath;
    /**
     * 是否开启测试环境
     */
    private Boolean isTestEnv;
}
