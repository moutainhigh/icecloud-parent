package com.icetech.cloudcenter.rpc;

import com.icetech.api.cloudcenter.service.DiscountFeignApi;
import com.icetech.cloudcenter.dao.order.OrderDiscountDao;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.domain.OrderDiscount;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.domain.response.PageQuery;
import com.icetech.common.util.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DiscountFeignClient implements DiscountFeignApi {
    // 日志管理
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OrderDiscountDao orderDiscountDao;


    @Override
    public ObjectResponse<List<OrderDiscount>> findList(PageQuery<OrderDiscount> pageQuery) {
        if (pageQuery.getParam() == null){
            return ResponseUtils.returnErrorResponse(CodeConstants.ERROR_400);
        }else{
            List<OrderDiscount> orderPays = orderDiscountDao.selectList(pageQuery);

            return ResponseUtils.returnSuccessResponse(orderPays);
        }
    }
}
