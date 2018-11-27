/*
 * Copyright (c) 2018. paascloud.net All Rights Reserved.
 * 项目名称：paascloud快速搭建企业级分布式微服务平台
 * 类名称：AuthorizationFeignApi.java
 * 创建人：刘兆明
 * 联系方式：paascloud.net@gmail.com
 * 开源地址: https://github.com/paascloud
 * 博客地址: http://blog.paascloud.net
 * 项目官网: http://paascloud.net
 */

package com.icetech.api.paycenter.service;


import com.icetech.api.paycenter.model.request.PayCenterBaseRequestDto;
import com.icetech.api.paycenter.service.hystrix.AuthorizationFeignApiHystrix;
import com.icetech.common.domain.response.ObjectResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 支付中心
 * @author wangzw
 */
@FeignClient(value = "icecloud-paycenter", fallback = AuthorizationFeignApiHystrix.class)
public interface AuthorizationFeignApi {
    /**
     * 获取授权请求地址
     * @param parkCode
     * @return
     */
    @PostMapping(value = "/api/paycenter/getAuthLink4Wx")
    ObjectResponse<String> getAuthLink4Wx(@RequestParam("parkCode") String parkCode);


    /**
     * 获取授权请求地址
     * @param parkCode
     * @return
     */
    @PostMapping(value = "/api/paycenter/getAuthLink4Ali")
    ObjectResponse<String> getAuthLink4Ali(@RequestParam("parkCode") String parkCode);

    /**
     * 获取微信openid
     * @param wxCode 微信授权码
     * @param parkCode 车场编号
     * @return
     */
    @PostMapping(value = "/api/paycenter/getWxOpenId")
    ObjectResponse<String> getWxOpenId(@RequestParam("wxCode") String wxCode,@RequestParam("parkCode") String parkCode);

    /**
     * 获取支付宝userId
     * @param aliCode 支付宝授权码
     * @param parkCode 车场编号
     * @return
     */
    @PostMapping(value = "/api/uac/message/getAliUserId")
    ObjectResponse<String> getAliUserId(@RequestParam("aliCode") String aliCode,@RequestParam("parkCode") String parkCode);

    /**
     * 验证签名
     * @param baseRequest
     * @return
     */
    @PostMapping(value = "/api/paycenter/verifySign")
    ObjectResponse<Boolean> verifySign(@RequestBody PayCenterBaseRequestDto baseRequest);
}
