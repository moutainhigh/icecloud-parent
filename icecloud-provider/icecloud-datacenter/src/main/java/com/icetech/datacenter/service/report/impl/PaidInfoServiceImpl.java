package com.icetech.datacenter.service.report.impl;

import com.icetech.api.cloudcenter.service.OrderPayFeignApi;
import com.icetech.common.DataChangeTools;
import com.icetech.common.ResultTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.constants.PayStatusConstants;
import com.icetech.common.domain.OrderPay;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.domain.response.PageQuery;
import com.icetech.common.util.ResponseUtils;
import com.icetech.datacenter.domain.request.DataCenterBaseRequest;
import com.icetech.datacenter.domain.request.PaidInfoRequest;
import com.icetech.datacenter.domain.response.PaidInfoResponse;
import com.icetech.datacenter.service.AbstractService;
import com.icetech.datacenter.service.report.ReportService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaidInfoServiceImpl extends AbstractService implements ReportService {

    @Autowired
    private OrderPayFeignApi orderPayService;

    @Override
    public String report(DataCenterBaseRequest dataCenterBaseRequest, Long parkId) {
        PaidInfoRequest paidInfoRequest = DataChangeTools.convert2bean(dataCenterBaseRequest.getBizContent(), PaidInfoRequest.class);

        //参数校验
        verifyParams(paidInfoRequest);

        /**
         * 开始处理业务
         */
        List<OrderPay> orderPays = getOrderPays(parkId, paidInfoRequest);
        List<PaidInfoResponse> paidInfoResponses = new ArrayList<>();
        for (int i=0;i<orderPays.size();i++){
            OrderPay orderPay1 = orderPays.get(i);
            PaidInfoResponse paidInfoResponse = new PaidInfoResponse();
            BeanUtils.copyProperties(orderPay1, paidInfoResponse);
            paidInfoResponses.add(paidInfoResponse);
        }
        if (paidInfoResponses.size() <= 0){
            return ResultTools.setResponse(CodeConstants.ERROR_404, CodeConstants.getName(CodeConstants.ERROR_404));
        }else{
            return ResultTools.setResponse(CodeConstants.SUCCESS, CodeConstants.getName(CodeConstants.SUCCESS), paidInfoResponses);
        }
    }

    private List<OrderPay> getOrderPays(Long parkId, PaidInfoRequest paidInfoRequest) {
        PageQuery<OrderPay> payPageQuery = new PageQuery<>();
        OrderPay orderPay = new OrderPay();
        orderPay.setParkId(parkId);
        orderPay.setOrderNum(paidInfoRequest.getOrderNum());
        orderPay.setPayStatus(PayStatusConstants.PAID);
        payPageQuery.setParam(orderPay);
        ObjectResponse<List<OrderPay>> objectResponse = orderPayService.findList(payPageQuery);
        ResponseUtils.notError(objectResponse);
        return objectResponse.getData();
    }

}
