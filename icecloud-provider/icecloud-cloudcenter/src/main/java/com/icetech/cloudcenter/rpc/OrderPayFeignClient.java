package com.icetech.cloudcenter.rpc;

import com.icetech.api.cloudcenter.service.OrderPayFeignApi;
import com.icetech.cloudcenter.dao.order.OrderInfoDao;
import com.icetech.cloudcenter.dao.order.OrderPayDao;
import com.icetech.common.ToolsUtil;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.domain.OrderInfo;
import com.icetech.common.domain.OrderPay;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.domain.response.PageQuery;
import com.icetech.common.util.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderPayFeignClient implements OrderPayFeignApi {
    // 日志管理
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OrderPayDao orderPayDao;
    @Autowired
    private OrderInfoDao orderInfoDao;

    @Override
    public ObjectResponse addOrderPay(OrderPay orderPay) {
        String orderNum = orderPay.getOrderNum();
        if (ToolsUtil.isNotNull(orderNum)){
            if (orderPay.getParkId() == null) {
                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setOrderNum(orderNum);
                orderInfo = orderInfoDao.selectById(orderInfo);
                orderPay.setParkId(orderInfo.getParkId());
            }
            orderPayDao.insert(orderPay);
            logger.info("<交易记录服务> 插入交易记录完成，tradeNo：{}", orderPay.getTradeNo());

            return ResponseUtils.returnSuccessResponse();
        }else{
            return ResponseUtils.returnErrorResponse(CodeConstants.ERROR_400);
        }

    }

    @Override
    public ObjectResponse<OrderPay> findOne(OrderPay orderPay) {
        OrderPay orderPay_res = orderPayDao.selectById(orderPay);
        logger.info("<交易记录服务> 查询单条交易记录完成，orderPay：{}", orderPay);

        return ResponseUtils.returnSuccessResponse(orderPay_res);
    }

    @Override
    public ObjectResponse modifyOrderPay(OrderPay orderPay) {
        Integer n = orderPayDao.update(orderPay);
        if (n > 0){
            logger.info("<交易记录服务> 更新交易记录未成功，参数：{}", orderPay);
            return ResponseUtils.returnSuccessResponse();
        }
        return ResponseUtils.returnErrorResponse(CodeConstants.ERROR_402);
    }

    @Override
    public ObjectResponse<List<OrderPay>> findList(PageQuery<OrderPay> payPageQuery) {
        if (payPageQuery.getParam() == null){
            return ResponseUtils.returnErrorResponse(CodeConstants.ERROR_400);
        }else{
            List<OrderPay> orderPays = orderPayDao.selectList(payPageQuery);
            return ResponseUtils.returnSuccessResponse(orderPays);
        }
    }
}
