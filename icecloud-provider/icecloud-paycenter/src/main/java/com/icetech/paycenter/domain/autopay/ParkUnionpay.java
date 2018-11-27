package com.icetech.paycenter.domain.autopay;

import lombok.Data;

import java.util.Date;

/**
 * 银联免密支付
 * @author fangct
 */
@Data
public class ParkUnionpay {
    /**
     * 主键自增长id
     */
    private Integer id;

    /**
     * 车场编号
     */
    private String parkCode;

    /**
     * 车场名称
     */
    private String parkName;

    /**
     * 商户代码
     */
    private String merCode;

    /**
     * 由银联无感支付平台分配，停车场唯一标识
     */
    private String outParkcode;

    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 车厂请求地址
     */
    private String autoPayUrl;

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