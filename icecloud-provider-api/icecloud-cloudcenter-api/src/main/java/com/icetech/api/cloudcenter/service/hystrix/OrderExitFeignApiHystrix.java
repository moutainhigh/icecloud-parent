package com.icetech.api.cloudcenter.service.hystrix;

import com.icetech.api.cloudcenter.model.request.ExitRequest;
import com.icetech.api.cloudcenter.service.OrderExitFeignApi;
import com.icetech.common.domain.response.ObjectResponse;
import org.springframework.stereotype.Component;

/**
 * Description : 车辆离场服务接口
 * @author fangct
 */
@Component
public class OrderExitFeignApiHystrix implements OrderExitFeignApi {

    @Override
    public ObjectResponse exit(ExitRequest exitRequest, String parkCode) {
        return null;
    }
}
