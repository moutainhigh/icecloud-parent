package com.icetech.paycenter.mapper.autopay;

import com.icetech.common.dao.BaseDao;
import com.icetech.paycenter.domain.autopay.ParkUnionpay;
import org.apache.ibatis.annotations.Param;

public interface ParkUnionpayDao extends BaseDao<ParkUnionpay> {
    ParkUnionpay selectByParkCode(@Param("parkCode") String parkCode);
}