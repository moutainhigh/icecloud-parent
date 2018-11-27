package com.icetech.transcenter.domain.response;

import com.icetech.transcenter.domain.BaseDomain;
import lombok.Data;

/**
 * app 端查询车辆实体响应
 * @author wangzw
 */
@Data
public class AppSearchCarResponse extends BaseDomain {
    /**
     * 完整的车牌
     */
    private String plateNum;
    /**
     * 云平台订单号
     */
    private String orderNum;
    /**
     * 入场时间，格式为yyyy-MM-dd 24hh:mi:ss
     */
    private String enterTime;
    /**
     * 车辆类型，1：临时车, 2：月卡车
     */
    private Integer carType;
    /**
     * 支付状态， 1：无需支付，2：待支付，3：已缴费，4：缴费超时
     */
    private Integer payStatus;
    /**
     * 车辆进场图片URL
     */
    private String enterImage;
}
