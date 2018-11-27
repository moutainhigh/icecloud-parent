package com.icetech.paycenter.domain.autopay;

import lombok.Getter;
import lombok.Setter;

/**
 * 免密支付订单表
 * @author fangct
 */
@Getter
@Setter
public class AutopayOrder {
    /**
     * 主键自增长id
     */
    private Integer id;

    /**
     * 车场编号
     */
    private String parkCode;

    /**
     * 订单号
     */
    private String orderNum;

    /**
     * 车牌号
     */
    private String plateNum;

    /**
     * 入场时间
     */
    private String enterTime;

    /**
     * 离场时间
     */
    private String exitTime;

    /**
     * 是否开通免密
     */
    private Integer isOpen;

    /**
     * 支付金额
     */
    private String paidPrice;

    /**
     * 服务状态
     */
    private Integer serviceStatus;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新时间
     */
    private String updateTime;
}
