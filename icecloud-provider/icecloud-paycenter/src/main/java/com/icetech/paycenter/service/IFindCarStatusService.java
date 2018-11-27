package com.icetech.paycenter.service;

import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.paycenter.domain.autopay.request.FindCarStatusRequest;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

/**
 * 查询车辆场内状态
 * @author fangct
 */
public interface IFindCarStatusService {

    /**
     * 查询车辆场内状态
     * @param findCarStatusRequest
     * @return
     */
    ObjectResponse findCarStatus(FindCarStatusRequest findCarStatusRequest) throws IllegalAccessException, IntrospectionException, InvocationTargetException;

}
