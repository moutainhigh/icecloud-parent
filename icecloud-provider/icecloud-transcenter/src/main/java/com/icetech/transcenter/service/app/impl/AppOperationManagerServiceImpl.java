package com.icetech.transcenter.service.app.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.icetech.api.cloudcenter.model.request.QueryOrderFeeRequest;
import com.icetech.api.cloudcenter.model.request.SearchCarRequest;
import com.icetech.api.cloudcenter.model.response.QueryOrderFeeResponse;
import com.icetech.api.cloudcenter.model.response.SearchCarResponse;
import com.icetech.api.cloudcenter.service.OrderFeignApi;
import com.icetech.api.cloudcenter.service.QueryOrderFeeFeginApi;
import com.icetech.api.datacenter.model.request.PrepayReportRequest;
import com.icetech.api.datacenter.service.ExternalFeignApi;
import com.icetech.api.paycenter.model.request.PayResultRequestDto;
import com.icetech.api.paycenter.model.request.UnifiedOrderRequestDto;
import com.icetech.api.paycenter.model.response.PayResultResponseDto;
import com.icetech.api.paycenter.model.response.UnifiedOrderResponseDto;
import com.icetech.api.paycenter.service.PayCenterFeignApi;
import com.icetech.common.DateTools;
import com.icetech.common.JsonTools;
import com.icetech.common.ResultTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.domain.response.PageQuery;
import com.icetech.transcenter.domain.request.*;
import com.icetech.transcenter.domain.response.AppPayResultResponse;
import com.icetech.transcenter.domain.response.AppPullFeeResponse;
import com.icetech.transcenter.domain.response.AppSearchCarResponse;
import com.icetech.transcenter.service.app.IAppOperationManagerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

/**
 * 运维app管理平台接口
 * @author wangzw
 */
@Service
public class AppOperationManagerServiceImpl implements IAppOperationManagerService {

    @Autowired
    private PayCenterFeignApi payCenterService;
    @Autowired
    private QueryOrderFeeFeginApi queryOrderFeeService;
    @Autowired
    private OrderFeignApi orderService;
    @Autowired
    private ExternalFeignApi externalService;
    @Override
    public String searchCar(AppSearchCarRequest request) {
        PageQuery<SearchCarRequest> pageQuery = new PageQuery<>(request.getPage(),request.getPageSize());
        SearchCarRequest searchCarRequest = new SearchCarRequest();
        searchCarRequest.setParkCode(request.getParkCode());
        searchCarRequest.setPlateNum(request.getPlateNum());
        pageQuery.setParam(searchCarRequest);
        ObjectResponse<List<SearchCarResponse>> response = orderService.searchCarInfo(pageQuery);
        if (response.getCode().equals(CodeConstants.SUCCESS)){
            List<AppSearchCarResponse> responses = Lists.newArrayList();
            response.getData().forEach(data->{
                AppSearchCarResponse appSearchCarResponse = new AppSearchCarResponse();
                BeanUtils.copyProperties(data,appSearchCarResponse);
                responses.add(appSearchCarResponse);
            });
            return ResultTools.setResponse(response.getCode(),response.getMsg(),responses);
        }
        return ResultTools.setResponse(response.getCode(),response.getMsg());
    }

    @Override
    public String queryFee(AppPullFeeRequest request) {
        QueryOrderFeeRequest queryOrderFeeRequest = new QueryOrderFeeRequest();
        queryOrderFeeRequest.setOrderNum(request.getOrderNum());
        ObjectResponse<QueryOrderFeeResponse> response = queryOrderFeeService.queryOrderFee(queryOrderFeeRequest);
        if (response.getCode().equals(CodeConstants.SUCCESS)){
            AppPullFeeResponse appPullFeeResponse = new AppPullFeeResponse();
            QueryOrderFeeResponse responseData = response.getData();
            appPullFeeResponse.setDiscountAmount(responseData.getDiscountAmount());
            appPullFeeResponse.setDiscountPrice(responseData.getDiscountPrice());
            appPullFeeResponse.setEnterTime(DateTools.getFormat(responseData.getEnterTime()*1000));
            appPullFeeResponse.setOrderNum(responseData.getOrderNum());
            appPullFeeResponse.setPaidAmount(responseData.getPaidAmount());
            appPullFeeResponse.setParkTime(responseData.getParkTime());
            appPullFeeResponse.setPayTime(responseData.getPayTime());
            appPullFeeResponse.setQueryTime(responseData.getQueryTime());
            appPullFeeResponse.setTotalAmount(responseData.getTotalAmount());
            appPullFeeResponse.setUnpayPrice(responseData.getUnpayPrice());
            return ResultTools.setResponse(response.getCode(),response.getMsg(),appPullFeeResponse);
        }
        return ResultTools.setResponse(response.getCode(),response.getMsg());
    }

    @Override
    public String doUnifiedOrder(AppUnifiedOrderRequest request) throws UnknownHostException {
        UnifiedOrderRequestDto unifiedOrderRequest = new UnifiedOrderRequestDto();
        unifiedOrderRequest.setParkCode(request.getParkCode());
        unifiedOrderRequest.setSelectTradeType(request.getTradeType());
        unifiedOrderRequest.setDeviceInfo("APP");
        unifiedOrderRequest.setTradeNo(request.getTradeNo());
        unifiedOrderRequest.setSpbillCreateIp(InetAddress.getLocalHost().getHostAddress());
        unifiedOrderRequest.setPrice(request.getPrice());
        unifiedOrderRequest.setProductInfo(request.getProductInfo());
        ObjectResponse<UnifiedOrderResponseDto> response = payCenterService.doUnifiedOrder(unifiedOrderRequest);
        if (response.getCode().equals(CodeConstants.SUCCESS)){
            Map params = Maps.newHashMap();
            params.put("QRCodeUrl",response.getData().getMapPayInfo().get("payUrl"));
            return ResultTools.setResponse(response.getCode(),response.getMsg(),params);
        }
        return ResultTools.setResponse(response.getCode(),response.getMsg());
    }

    @Override
    public String queryPayResult(AppPayResultRequest request) {
        PayResultRequestDto payResultRequest = new PayResultRequestDto();
        payResultRequest.setParkCode(request.getParkCode());
        payResultRequest.setTradeNo(request.getTradeNo());
        ObjectResponse<PayResultResponseDto> response = payCenterService.getPayResult(payResultRequest, request.getTradeType());
        if (response.getCode().equals(CodeConstants.SUCCESS)){
            PayResultResponseDto payResultResponse = response.getData();
            AppPayResultResponse appPayResultResponse = new AppPayResultResponse();
            appPayResultResponse.setPrice(payResultResponse.getPrice());
            appPayResultResponse.setTradeStatus(payResultResponse.getTradeStatus());
            return ResultTools.setResponse(response.getCode(),response.getMsg(),appPayResultResponse);
        }
        return ResultTools.setResponse(response.getCode(),response.getMsg());
    }

    @Override
    public String notifyPrepay(AppPrePaymentNotifyRequest request) {
        PrepayReportRequest prepayReportRequest = new PrepayReportRequest();
        prepayReportRequest.setDiscountNos(request.getDiscountNos());
        prepayReportRequest.setDiscountPrice(request.getDiscountPrice());
        prepayReportRequest.setOrderNum(request.getOrderNum());
        prepayReportRequest.setPaidPrice(request.getPrepay());
        prepayReportRequest.setTotalPrice(request.getTotalPrice());
        prepayReportRequest.setPayChannel(request.getPayChannel());
        prepayReportRequest.setPayTerminal(request.getPayTerminal());
        prepayReportRequest.setPayTime(request.getPayTime());
        prepayReportRequest.setPayWay(request.getPayWay());
        prepayReportRequest.setPlateNum(request.getPlateNum());
        prepayReportRequest.setTradeNo(request.getTradeNo());
        ObjectResponse response = externalService.prepayReport(prepayReportRequest);
        return JsonTools.toString(response);
    }
}
