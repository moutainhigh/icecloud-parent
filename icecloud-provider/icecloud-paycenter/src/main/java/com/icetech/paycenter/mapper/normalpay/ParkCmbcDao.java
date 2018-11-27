package com.icetech.paycenter.mapper.normalpay;


import com.icetech.common.dao.BaseDao;
import com.icetech.paycenter.domain.ParkCmbc;

public interface ParkCmbcDao extends BaseDao<ParkCmbc> {

    ParkCmbc selectByParkCode(String parkCode);
}