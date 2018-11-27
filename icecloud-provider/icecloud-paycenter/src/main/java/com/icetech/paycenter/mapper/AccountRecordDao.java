package com.icetech.paycenter.mapper;

import com.icetech.common.dao.BaseDao;
import com.icetech.paycenter.domain.AccountRecord;
import org.apache.ibatis.annotations.Param;


public interface AccountRecordDao extends BaseDao<AccountRecord> {

    /**
     * 根据parkcode 和 交易时产生的订单号查询交易记录
     * @param parkCode
     * @param tradeNo 交易流水号
     * @return
     */
    AccountRecord selectByParkCodeAndTradeNo(@Param("parkCode") String parkCode, @Param("tradeNo") String tradeNo);


    /**
     * 根据外部订单号获取流水记录
     * @param outTradeNo
     * @return
     */
    AccountRecord selectByOutTradeNo(@Param("outTradeNo") String outTradeNo);
}