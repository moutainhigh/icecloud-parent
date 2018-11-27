package com.icetech.api.paycenter.service;

import com.icetech.api.paycenter.model.request.*;
import com.icetech.api.paycenter.model.response.PayResultResponseDto;
import com.icetech.api.paycenter.model.response.UnifiedOrderResponseDto;
import com.icetech.api.paycenter.service.hystrix.PayCenterFeignApiHystrix;
import com.icetech.common.domain.response.ObjectResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 支付中心 支付 接口
 * @author wangzw
 */
@FeignClient(value = "icecloud-paycenter", fallback = PayCenterFeignApiHystrix.class)
public interface PayCenterFeignApi {

    /**
     * 统一下单
     * @param unifiedOrderRequest
     * @return
     */
    @PostMapping(value = "/api/paycenter/doUnifiedOrder")
    ObjectResponse<UnifiedOrderResponseDto> doUnifiedOrder(UnifiedOrderRequestDto unifiedOrderRequest);
    /**
     * 离场免密支付
     * @param exitpayRequest
     * @return
     */
    @PostMapping(value = "/api/paycenter/autoPay")
    ObjectResponse autoPay(ExitpayRequestDto exitpayRequest);

    /**
     * 查询微信支付宝 支付结果
     * @param request 支付结果查询请求
     * @param tradeType 支付类型
     * @return
     */
    @PostMapping(value = "/api/paycenter/getPayResult")
    ObjectResponse<PayResultResponseDto> getPayResult(PayResultRequestDto request, String tradeType);

    /**
     * 入场通知
     * @param request 请求参数
     * @return
     */
    @PostMapping(value = "/api/paycenter/autoPayEnterNotify")
    ObjectResponse<String> autoPayEnterNotify(EnterNotifyRequestDto request);

    /**
     * 离场通知
     * @param request 请求参数
     * @return
     */
    @PostMapping(value = "/api/paycenter/autoPayExitNotify")
    ObjectResponse<String> autoPayExitNotify(ExitNotifyRequestDto request);
}
