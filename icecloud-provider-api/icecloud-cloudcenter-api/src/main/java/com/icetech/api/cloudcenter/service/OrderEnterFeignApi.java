package com.icetech.api.cloudcenter.service;

import com.icetech.api.cloudcenter.model.request.EnterRequest;
import com.icetech.common.domain.response.ObjectResponse;

import java.util.Map;

/**
 * Description : 车辆进场服务接口
 * @author fangct
 */
public interface OrderEnterFeignApi {

    /**
     * 车辆进场业务
     * @param enterRequest
     * @return
     */
    ObjectResponse<Map<String, Object>> enter(EnterRequest enterRequest, String parkCode);

}
