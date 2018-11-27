package com.icetech.paycenter.common.tool;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.icetech.paycenter.common.config.WxConfig;
import com.icetech.paycenter.domain.ParkWeixin;

/**
 * @author: dingzhiwei
 * @date: 17/8/25
 * @description:
 */
public class WxPayUtil {

    /**
     * 获取微信支付配置
     * @param wxConfig
     * @param parkWeixin
     * @return
     */
    public static WxPayConfig getWxPayConfig(WxConfig wxConfig, ParkWeixin parkWeixin, String tradeType, String notifyUrl) {
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setMchId(parkWeixin.getMchId());
        wxPayConfig.setAppId(parkWeixin.getAppId());
        wxPayConfig.setKeyPath(parkWeixin.getCertPath());
        wxPayConfig.setMchKey(parkWeixin.getApiKey());
        wxPayConfig.setNotifyUrl(notifyUrl);
        wxPayConfig.setTradeType(tradeType);
        wxPayConfig.setUseSandboxEnv(wxConfig.getUseSandboxEnv());
        return wxPayConfig;
    }

    /**
     * 获取微信支付配置
     * @param wxConfig
     * @param parkWeixin
     * @return
     */
    public static WxPayConfig getWxPayConfig(WxConfig wxConfig, ParkWeixin parkWeixin) {
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setMchId(parkWeixin.getMchId());
        wxPayConfig.setAppId(parkWeixin.getAppId());
        wxPayConfig.setMchKey(parkWeixin.getApiKey());
        wxPayConfig.setUseSandboxEnv(wxConfig.getUseSandboxEnv());
        return wxPayConfig;
    }

}
