package com.icetech.paycenter.service;

import com.icetech.paycenter.domain.request.PayCenterBaseRequest;

/**
 * 对外API接口类
 * @author fangct
 */
public interface IApiService {

    /**
     * 接口方法
     * @param baseRequest 参数实体
     * @return
     */
    String execute(PayCenterBaseRequest baseRequest) throws Exception;
}
