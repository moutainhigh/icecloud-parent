package com.icetech.paycenter.domain;

import lombok.Data;

import java.util.Date;

/**
 * 
 */
@Data
public class ParkAlipay {
    /**
     * 主键自增长id
     */
    private Integer id;

    /**
     * 车场id
     */
    private String parkCode;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 商户ID
     */
    private String partnerId;

    /**
     * 商户私钥
     */
    private String privateKey;

    /**
     * 支付宝公钥
     */
    private String aliPublicKey;
    /**
     * 异步地址
     */
    private String notifyUrl;
    /**
     *同步返回地址
     */
    private String returnUrl;
    /**
     * 终断支付的退出 url
     */
    private String quitUrl;
    /**
     * 状态1启用2不启用默认1
     */
    private Integer status;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新时间
     */
    private String updateUser;
}