package com.icetech.paycenter.service.impl;

import com.icetech.common.ResultTools;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.paycenter.common.client.PayCenterClient;
import com.icetech.paycenter.domain.ThirdInfo;
import com.icetech.paycenter.domain.autopay.ParkUnionpay;
import com.icetech.paycenter.domain.autopay.request.FindCarStatusRequest;
import com.icetech.paycenter.domain.autopay.response.FindCarStatusResponse;
import com.icetech.paycenter.domain.request.PayCenterBaseRequest;
import com.icetech.paycenter.mapper.ThirdInfoDao;
import com.icetech.paycenter.mapper.autopay.ParkUnionpayDao;
import com.icetech.paycenter.service.IFindCarStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

/**
 * 查询车辆场内状态
 * @author fangct
 */
@Service
public class FindCarStatusServiceImpl implements IFindCarStatusService{

    private static final String SERVICE_NAME = "autopay.findCarStatus";

    @Autowired
    private PayCenterClient payCenterClient;
    @Autowired
    private ParkUnionpayDao parkUnionpayDao;
    @Autowired
    private ThirdInfoDao thirdInfoDao;
    @Override
    public ObjectResponse findCarStatus(FindCarStatusRequest findCarStatusRequest) throws IllegalAccessException, IntrospectionException, InvocationTargetException {

        PayCenterBaseRequest<FindCarStatusRequest> baseRequest = new PayCenterBaseRequest<>();
        baseRequest.setBizContent(findCarStatusRequest);
        baseRequest.setServiceName(SERVICE_NAME);
        //获取无感支付的配置
        ParkUnionpay parkUnionpay = parkUnionpayDao.selectByParkCode(findCarStatusRequest.getParkCode());
        System.out.println("查询车辆状态，parkCode："+findCarStatusRequest.getParkCode());
        //获取第三方配置
        ThirdInfo thirdInfo = thirdInfoDao.selectByParkCode(findCarStatusRequest.getParkCode());
        String result = payCenterClient.sendPark(baseRequest, thirdInfo,parkUnionpay.getAutoPayUrl());
        return ResultTools.getObjectResponse(result, FindCarStatusResponse.class);
    }
}
