package com.icetech.cloudcenter.dao.order;

import com.icetech.cloudcenter.domain.order.update.OrderInfoUpdate;
import com.icetech.common.dao.BaseDao;
import com.icetech.common.domain.OrderInfo;
import org.springframework.stereotype.Repository;

/**
 * Description : 订单信息表操作DAO
 * @author fangct 
 */
@Repository
public interface OrderInfoDao extends BaseDao<OrderInfo> {
    /**
     * 修改状态
     * @param orderInfoUpdate
     * @return
     */
    Integer updateStatus(OrderInfoUpdate orderInfoUpdate);

}
