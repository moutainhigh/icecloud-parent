package com.icetech.api.cloudcenter.service.hystrix;

import com.icetech.api.cloudcenter.model.request.SearchCarRequest;
import com.icetech.api.cloudcenter.model.response.SearchCarResponse;
import com.icetech.api.cloudcenter.service.OrderFeignApi;
import com.icetech.common.domain.OrderInfo;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.domain.response.PageQuery;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description : 订单服务接口
 * @author fangct
 */
@Component
public class OrderFeignApiHystrix implements OrderFeignApi {


    @Override
    public ObjectResponse<OrderInfo> findByOrderNum(String orderNum) {
        return null;
    }

    @Override
    public ObjectResponse<OrderInfo> findInPark(String plateNum, String parkCode) {
        return null;
    }

    @Override
    public ObjectResponse<List<SearchCarResponse>> searchCarInfo(PageQuery<SearchCarRequest> pageQuery) {
        return null;
    }
}
