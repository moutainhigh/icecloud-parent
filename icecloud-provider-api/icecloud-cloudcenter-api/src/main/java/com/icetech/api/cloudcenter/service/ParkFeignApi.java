package com.icetech.api.cloudcenter.service;


import com.icetech.common.domain.Park;
import com.icetech.common.domain.response.ObjectResponse;

/**
 * 车场服务接口
 * @author fangct
 */
public interface ParkFeignApi {

    ObjectResponse<Park> findByParkId(Long parkId);

    ObjectResponse<Park> findByParkCode(String parkCode);
}
