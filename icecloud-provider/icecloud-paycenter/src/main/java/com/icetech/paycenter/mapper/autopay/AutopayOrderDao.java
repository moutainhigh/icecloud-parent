package com.icetech.paycenter.mapper.autopay;

import com.icetech.common.dao.BaseDao;
import com.icetech.paycenter.domain.autopay.AutopayOrder;

/**
 * 无感支付订单表
 * @author fangct
 */
public interface AutopayOrderDao extends BaseDao<AutopayOrder> {

    /**
     * 更新无感支付的开启状态为开启
     * @param plateNum 车牌号
     * @return
     */
    int updateOpenStatusByPlateNum(String plateNum);

    /**
     * 查询场内的订单记录
     * @param plateNum
     * @return
     */
    AutopayOrder selectInParkByPlateNum(String plateNum);

}