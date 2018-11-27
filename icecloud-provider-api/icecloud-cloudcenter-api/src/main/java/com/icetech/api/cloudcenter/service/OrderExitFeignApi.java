package com.icetech.api.cloudcenter.service;

import com.icetech.api.cloudcenter.model.request.ExitRequest;
import com.icetech.common.domain.response.ObjectResponse;

/**
 * Description : 车辆离场服务接口
 * @author fangct
 */
public interface OrderExitFeignApi {

    /**
     * 车辆离场业务
     * @param exitRequest
     * @return
     */
    ObjectResponse exit(ExitRequest exitRequest,String parkCode);

}
