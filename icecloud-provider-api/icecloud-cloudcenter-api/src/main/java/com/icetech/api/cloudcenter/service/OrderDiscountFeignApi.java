package com.icetech.api.cloudcenter.service;


import com.icetech.api.cloudcenter.model.request.DiscountRequest;
import com.icetech.common.domain.response.ObjectResponse;

public interface OrderDiscountFeignApi {

    ObjectResponse modifyDiscount(DiscountRequest discountRequest);

    /**
     * 查询流水号对应的优惠
     * @param tradeNo
     * @param parkId
     * @return
     */
    ObjectResponse findDiscountNos(String tradeNo, Long parkId);

}
