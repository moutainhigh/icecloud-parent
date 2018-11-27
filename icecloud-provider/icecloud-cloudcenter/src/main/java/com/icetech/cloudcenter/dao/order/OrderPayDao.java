package com.icetech.cloudcenter.dao.order;

import com.icetech.cloudcenter.domain.order.query.OrderPayQuery;
import com.icetech.common.dao.BaseDao;
import com.icetech.common.domain.OrderPay;

/**
 * Description : 订单出入场记录表操作DAO
 * @author fangct 
 */
public interface OrderPayDao extends BaseDao<OrderPay> {

    int updateStatus(OrderPayQuery orderPayQuery);
}
