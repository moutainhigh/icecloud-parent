package com.icetech.api.cloudcenter.service;

import com.icetech.common.domain.OrderPay;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.domain.response.PageQuery;

import java.util.List;

public interface OrderPayFeignApi {

    /**
     * 添加交易记录
     * @param orderPay
     * @return
     */
    ObjectResponse addOrderPay(OrderPay orderPay);

    /**
     * 查询返回一条交易记录
     * @param orderPay
     * @return
     */
    ObjectResponse<OrderPay> findOne(OrderPay orderPay);

    /**
     * 查询返回一条交易记录
     * @param orderPay
     * @return
     */
    ObjectResponse modifyOrderPay(OrderPay orderPay);

    /**
     * 查询结果集
     * @param payPageQuery
     * @return
     */
    ObjectResponse<List<OrderPay>> findList(PageQuery<OrderPay> payPageQuery);
}
