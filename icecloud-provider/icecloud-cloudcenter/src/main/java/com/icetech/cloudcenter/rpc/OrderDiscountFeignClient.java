package com.icetech.cloudcenter.rpc;

import com.icetech.api.cloudcenter.model.request.DiscountRequest;
import com.icetech.api.cloudcenter.service.OrderDiscountFeignApi;
import com.icetech.cloudcenter.dao.order.OrderDiscountDao;
import com.icetech.cloudcenter.domain.order.query.OrderDiscountQuery;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.domain.OrderDiscount;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.domain.response.PageQuery;
import com.icetech.common.util.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("orderDiscountService")
public class OrderDiscountFeignClient implements OrderDiscountFeignApi {
    // 日志管理
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OrderDiscountDao orderDiscountDao;

    @Override
    public ObjectResponse modifyDiscount(DiscountRequest discountRequest) {

        OrderDiscountQuery orderDiscountQuery = new OrderDiscountQuery();
        BeanUtils.copyProperties(discountRequest, orderDiscountQuery);
        orderDiscountDao.updateStatus(orderDiscountQuery);
        logger.info("<订单优惠服务> 修改优惠状态完成，orderNum：{}", discountRequest.getOrderNum());

        return ResponseUtils.returnSuccessResponse();
    }

    @Override
    public ObjectResponse<Map<String, String>> findDiscountNos(String tradeNo, Long parkId) {
        OrderDiscount orderDiscount = new OrderDiscount();
        orderDiscount.setTradeNo(tradeNo);
        orderDiscount.setParkId(parkId);
        List<OrderDiscount> orderDiscounts = findList(orderDiscount);

        if (orderDiscounts.size() > 0){
            StringBuffer discountNos = new StringBuffer();
            for (OrderDiscount discount : orderDiscounts){
                discountNos.append(discount.getDiscountNo() + ",");
            }
            Map<String, String> map = new HashMap<>();
            map.put("discountNos", discountNos.toString());
            return ResponseUtils.returnSuccessResponse(map);
        }else{
            return ResponseUtils.returnErrorResponse(CodeConstants.ERROR_404);
        }
    }

    public List<OrderDiscount> findList(OrderDiscount orderDiscount){
        PageQuery<OrderDiscount> pageQuery = new PageQuery<OrderDiscount>();
        pageQuery.setParam(orderDiscount);
        List<OrderDiscount> orderDiscounts = orderDiscountDao.selectList(pageQuery);
        return orderDiscounts;
    }

}
