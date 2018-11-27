package com.icetech.datacenter.rpc;

import com.icetech.api.cloudcenter.model.request.DiscountRequest;
import com.icetech.api.cloudcenter.service.OrderDiscountFeignApi;
import com.icetech.api.cloudcenter.service.OrderFeignApi;
import com.icetech.api.cloudcenter.service.OrderPayFeignApi;
import com.icetech.api.datacenter.model.request.PrepayReportRequest;
import com.icetech.api.datacenter.service.ExternalFeignApi;
import com.icetech.common.constants.PayStatusConstants;
import com.icetech.common.domain.OrderInfo;
import com.icetech.common.domain.OrderPay;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.util.ResponseUtils;
import com.icetech.datacenter.service.AbstractService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExternalFeignClient extends AbstractService implements ExternalFeignApi {
    @Autowired
    private OrderDiscountFeignApi orderDiscountService;
    @Autowired
    private OrderPayFeignApi orderPayService;
    @Autowired
    private OrderFeignApi orderService;

    @Override
    public ObjectResponse prepayReport(PrepayReportRequest prepayReportRequest) {

        logger.info("<预缴费上报> 参数：{}", prepayReportRequest);

        /**
         * 开始处理业务
         */
        OrderPay orderPay = new OrderPay();
        BeanUtils.copyProperties(prepayReportRequest, orderPay);
        Long parkId = getParkId(prepayReportRequest);
        orderPay.setParkId(parkId);
        orderPay.setPayStatus(PayStatusConstants.PAID);

        ObjectResponse objectResponse = orderPayService.addOrderPay(orderPay);
        ResponseUtils.notError(objectResponse);
        String discountNos = prepayReportRequest.getDiscountNos();
        if (discountNos != null){

            String[] discountNoArr = discountNos.split(",");
            if (discountNoArr != null && discountNoArr.length > 0){

                DiscountRequest discountRequest = new DiscountRequest();
                BeanUtils.copyProperties(orderPay, discountRequest);
                discountRequest.setDiscountNos(discountNoArr);
                discountRequest.setNewStatus(2);

                objectResponse = orderDiscountService.modifyDiscount(discountRequest);
                ResponseUtils.notError(objectResponse);
                return ResponseUtils.returnSuccessResponse();
            }
        }
        return ResponseUtils.returnSuccessResponse();
    }

    /**
     * 获取parkId
     * @param prepayReportRequest
     * @return
     */
    private Long getParkId(PrepayReportRequest prepayReportRequest) {
        if (prepayReportRequest.getParkId() != null){
            return prepayReportRequest.getParkId();
        }else{
            ObjectResponse<OrderInfo> objectResponse_order = orderService.findByOrderNum(prepayReportRequest.getOrderNum());
            ResponseUtils.notError(objectResponse_order);
            return objectResponse_order.getData().getParkId();
        }

    }


}
