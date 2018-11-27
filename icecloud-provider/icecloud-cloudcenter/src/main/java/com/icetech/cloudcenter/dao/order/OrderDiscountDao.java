package com.icetech.cloudcenter.dao.order;

import com.icetech.cloudcenter.domain.order.query.OrderDiscountQuery;
import com.icetech.common.dao.BaseDao;
import com.icetech.common.domain.OrderDiscount;
import org.springframework.stereotype.Repository;

/**
 * Description : 订单优惠表操作DAO
 * @author fangct 
 */
@Repository
public interface OrderDiscountDao extends BaseDao<OrderDiscount> {

    /**
     * 修改优惠状态
     * @param orderDiscountQuery
     * @return
     */
    int updateStatus(OrderDiscountQuery orderDiscountQuery);

}
