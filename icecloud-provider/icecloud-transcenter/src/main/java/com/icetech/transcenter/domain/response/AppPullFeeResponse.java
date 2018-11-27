package com.icetech.transcenter.domain.response;

import com.icetech.transcenter.domain.BaseDomain;
import lombok.Data;

/**
 * app 拉去费用响应
 * @author wangzw
 */
@Data
public class AppPullFeeResponse extends BaseDomain {

    /**
     * 云平台订单号
     */
    private String orderNum;
    /**
     * 入场时间，格式为yyyy-MM-dd 24hh:mi:ss
     */
    private String enterTime;
    /**
     * 总金额，从车辆入场至当前时间计费的总费用
     */
    private String totalAmount;
    /**
     * 已缴金额
     */
    private String paidAmount;
    /**
     * 优惠金额，已使用过优惠总金额
     */
    private String discountAmount;
    /**
     * 本次优惠金额，从上次支付时间至当前时间优惠金额
     */
    private String discountPrice;
    /**
     * 本次应付金额，unpayPrice + discountPrice + discountAmount + paidAmount = totalAmount
     */
    private String unpayPrice;
    /**
     * 停车总时长，单位秒
     */
    private Long parkTime;
    /**
     * 最后支付时间（unix时间戳），如果没有过预缴费，则不用传
     */
    private Long payTime;
    /**
     * 当次计费的时间（unix时间戳）
     */
    private Long queryTime;

}
