package com.icetech.api.cloudcenter.service.hystrix;


import com.icetech.api.cloudcenter.service.ParkFeignApi;
import com.icetech.common.domain.Park;
import com.icetech.common.domain.response.ObjectResponse;
import org.springframework.stereotype.Component;

/**
 * 车场服务接口
 * @author fangct
 */
@Component
public class ParkFeignApiHystrix implements ParkFeignApi {

    @Override
    public ObjectResponse<Park> findByParkId(Long parkId) {
        return null;
    }

    @Override
    public ObjectResponse<Park> findByParkCode(String parkCode) {
        return null;
    }
}
