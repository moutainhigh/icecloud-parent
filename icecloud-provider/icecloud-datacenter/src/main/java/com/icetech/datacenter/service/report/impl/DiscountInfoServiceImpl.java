package com.icetech.datacenter.service.report.impl;

import com.icetech.api.cloudcenter.service.DiscountFeignApi;
import com.icetech.common.DataChangeTools;
import com.icetech.common.DateTools;
import com.icetech.common.ResultTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.domain.OrderDiscount;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.domain.response.PageQuery;
import com.icetech.common.util.ResponseUtils;
import com.icetech.datacenter.domain.request.DataCenterBaseRequest;
import com.icetech.datacenter.domain.request.DiscountInfoRequest;
import com.icetech.datacenter.domain.response.DiscountInfoResponse;
import com.icetech.datacenter.service.AbstractService;
import com.icetech.datacenter.service.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DiscountInfoServiceImpl extends AbstractService implements ReportService {

    @Autowired
    private DiscountFeignApi discountService;

    @Override
    public String report(DataCenterBaseRequest dataCenterBaseRequest, Long parkId) {
        DiscountInfoRequest discountInfoRequest = DataChangeTools.convert2bean(dataCenterBaseRequest.getBizContent(), DiscountInfoRequest.class);

        //参数校验
        verifyParams(discountInfoRequest);

        /**
         * 开始处理业务
         */
        List<OrderDiscount> orderDiscounts = getOrderPays(parkId, discountInfoRequest);
        List<DiscountInfoResponse> discountInfoResponses = new ArrayList<DiscountInfoResponse>();
        for (int i=0;i<orderDiscounts.size();i++){
            OrderDiscount orderDiscount = orderDiscounts.get(i);
            DiscountInfoResponse discountInfoResponse = new DiscountInfoResponse();
            discountInfoResponse.setDiscountNo(orderDiscount.getDiscountNo());
            discountInfoResponse.setDiscountType(orderDiscount.getType());
            discountInfoResponse.setDiscountNumber(orderDiscount.getAmount());
            discountInfoResponse.setDiscountTime(DateTools.timeStr2seconds(orderDiscount.getSendTime()));
            discountInfoResponses.add(discountInfoResponse);
        }
        if (discountInfoResponses.size() <= 0){
            return ResultTools.setResponse(CodeConstants.ERROR_404, CodeConstants.getName(CodeConstants.ERROR_404));
        }else{
            return ResultTools.setResponse(CodeConstants.SUCCESS, CodeConstants.getName(CodeConstants.SUCCESS), discountInfoResponses);
        }
    }

    private List<OrderDiscount> getOrderPays(Long parkId, DiscountInfoRequest discountInfoRequest) {
        PageQuery<OrderDiscount> payPageQuery = new PageQuery<OrderDiscount>();
        OrderDiscount orderDiscount = new OrderDiscount();
        orderDiscount.setParkId(parkId);
        orderDiscount.setOrderNum(discountInfoRequest.getOrderNum());
        payPageQuery.setParam(orderDiscount);
        ObjectResponse<List<OrderDiscount>> objectResponse = discountService.findList(payPageQuery);
        ResponseUtils.notError(objectResponse);
        return objectResponse.getData();
    }

}
