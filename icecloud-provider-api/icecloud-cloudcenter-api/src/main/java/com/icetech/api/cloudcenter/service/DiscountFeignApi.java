package com.icetech.api.cloudcenter.service;

import com.icetech.common.domain.OrderDiscount;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.domain.response.PageQuery;

import java.util.List;

public interface DiscountFeignApi {

    /**
     * 查询结果集
     * @param pageQuery
     * @return
     */
    ObjectResponse<List<OrderDiscount>> findList(PageQuery<OrderDiscount> pageQuery);
}
