package com.icetech.api.cloudcenter.service.hystrix;

import com.icetech.api.cloudcenter.model.request.EnterRequest;
import com.icetech.api.cloudcenter.service.OrderEnterFeignApi;
import com.icetech.common.domain.response.ObjectResponse;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Description : 车辆进场服务接口
 * @author fangct
 */
@Component
public class OrderEnterFeignApiHystrix implements OrderEnterFeignApi {



    @Override
    public ObjectResponse<Map<String, Object>> enter(EnterRequest enterRequest, String parkCode) {
        return null;
    }
}
