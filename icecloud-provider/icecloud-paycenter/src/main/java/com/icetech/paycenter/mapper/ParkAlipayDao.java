package com.icetech.paycenter.mapper;


import com.icetech.common.dao.BaseDao;
import com.icetech.paycenter.domain.ParkAlipay;

public interface ParkAlipayDao extends BaseDao<ParkAlipay> {

    ParkAlipay selectByParkCode(String parkCode);
}