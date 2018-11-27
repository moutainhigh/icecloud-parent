package com.icetech.api.cloudcenter.service;

import com.icetech.api.cloudcenter.model.request.SearchCarRequest;
import com.icetech.api.cloudcenter.model.response.SearchCarResponse;
import com.icetech.common.domain.OrderInfo;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.domain.response.PageQuery;

import java.util.List;

/**
 * Description : 订单服务接口
 * @author fangct
 */
public interface OrderFeignApi {

    /**
     * 根据订单号查询
     * @param orderNum
     * @return
     */
    ObjectResponse<OrderInfo> findByOrderNum(String orderNum);

    /**
     * 根据车牌号查询场内订单
     * @param plateNum 必填
     * @param parkCode
     * @return
     */
    ObjectResponse<OrderInfo> findInPark(String plateNum, String parkCode);

    /**
     * 查询车辆信息
     * @param pageQuery
     * @return
     */
    ObjectResponse<List<SearchCarResponse>> searchCarInfo(PageQuery<SearchCarRequest> pageQuery);



}
