package com.icetech.paycenter.mapper;

import com.icetech.common.dao.BaseDao;
import com.icetech.paycenter.domain.ParkWeixin;

public interface ParkWeixinDao extends BaseDao<ParkWeixin> {

    ParkWeixin selectByParkCode(String parkCode);
}