package com.icetech.cloudcenter.dao.park;

import com.icetech.common.domain.Park;

public interface ParkDao {

    Park selectById(Long parkId);

    Park selectByCode(String parkCode);
}
