package com.icetech.paycenter.service.autopay.impl;

import com.icetech.common.DataChangeTools;
import com.icetech.common.ResultTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.domain.response.Response;
import com.icetech.common.validator.Validator;
import com.icetech.paycenter.domain.autopay.AutopayOrder;
import com.icetech.paycenter.domain.autopay.request.EnterNotifyRequest;
import com.icetech.paycenter.domain.autopay.request.ExitNotifyRequest;
import com.icetech.paycenter.domain.autopay.request.FindCarStatusRequest;
import com.icetech.paycenter.domain.autopay.response.FindCarStatusResponse;
import com.icetech.paycenter.domain.request.PayCenterBaseRequest;
import com.icetech.paycenter.mapper.autopay.AutopayOrderDao;
import com.icetech.paycenter.service.IApiService;
import com.icetech.paycenter.service.impl.FindCarStatusServiceImpl;
import com.icetech.paycenter.service.impl.PayCenter4UnionServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * 银联无感支付
 *
 * @author fangct
 */
@Service
public class AutopayEnterNotifyServiceImpl implements IApiService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PayCenter4UnionServiceImpl unionPayCenterService;
    @Autowired
    private AutopayOrderDao autopayOrderDao;

    @Override
    public String execute(PayCenterBaseRequest baseRequest) throws Exception {
        EnterNotifyRequest enterNotifyRequest = DataChangeTools.convert2bean(baseRequest.getBizContent(), EnterNotifyRequest.class);

        /**
         * 参数校验
         */
        if (Validator.validate(enterNotifyRequest)) {

            String parkCode = enterNotifyRequest.getParkCode();
            String orderNum = enterNotifyRequest.getOrderNum();
            String plateNum = enterNotifyRequest.getPlateNum();

            AutopayOrder autopayOrder = new AutopayOrder();
            autopayOrder.setParkCode(parkCode);
            autopayOrder.setOrderNum(orderNum);
            autopayOrder = autopayOrderDao.selectById(autopayOrder);

            //根据车场编号和订单号未找到订单记录
            if (autopayOrder == null) {

                //如果该车牌有在场内记录，则返回“车辆有未离场记录”
                AutopayOrder autopayOrder_res = autopayOrderDao.selectInParkByPlateNum(plateNum);
                if (autopayOrder_res != null) {
                    /**
                     *请求车场查询车辆状态
                     */
                    Map resMap = findCarStatus(parkCode, plateNum, autopayOrder_res.getOrderNum());
                    if ((Boolean) resMap.get("isExit")){
                        logger.info("车牌号：{}，订单号：{} 已经离场，开始请求银联出场通知.", plateNum, orderNum);
                        sendExitRequest(parkCode, plateNum, autopayOrder_res.getOrderNum(), (String)resMap.get("exitTime"));
                    }else {
                        return ResultTools.setResponse(CodeConstants.ERROR_2004, CodeConstants.getName(CodeConstants.ERROR_2004));
                    }
                }

                //请求入场通知
                String result = unionPayCenterService.enterNotify(enterNotifyRequest);
                //准备无感支付的订单实体
                autopayOrder = new AutopayOrder();
                autopayOrder.setParkCode(enterNotifyRequest.getParkCode());
                autopayOrder.setOrderNum(enterNotifyRequest.getOrderNum());
                autopayOrder.setPlateNum(enterNotifyRequest.getPlateNum());
                autopayOrder.setEnterTime(enterNotifyRequest.getEnterTime());
                autopayOrder.setServiceStatus(ENTER);
                Response response = DataChangeTools.gson2bean(result, Response.class);
                if (CodeConstants.SUCCESS.equals(response.getCode())) {
                    autopayOrder.setIsOpen(1);
                }
                autopayOrderDao.insert(autopayOrder);
                return result;

            } else {
                //根据车场编号和订单号找到了重复记录
                return ResultTools.setResponse(CodeConstants.ERROR_405, CodeConstants.getName(CodeConstants.ERROR_405));
            }
        } else {
            return ResultTools.setResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));
        }
    }

    private static final int EXIT = 2;
    private static final int ENTER = 1;

    /**
     * 发送银联离场请求
     * @param parkCode
     * @param plateNum
     * @param orderNum
     * @param exitTime
     */
    private void sendExitRequest(String parkCode, String plateNum, String orderNum, String exitTime) {
        ExitNotifyRequest exitNotifyRequest = new ExitNotifyRequest();
        exitNotifyRequest.setParkCode(parkCode);
        exitNotifyRequest.setOrderNum(orderNum);
        exitNotifyRequest.setPlateNum(plateNum);
        exitNotifyRequest.setExitTime(exitTime);
        String result = unionPayCenterService.exitNotify(exitNotifyRequest);

        Response response = DataChangeTools.gson2bean(result, Response.class);
        if (CodeConstants.SUCCESS.equals(response.getCode())) {
            //准备无感支付的订单实体
            AutopayOrder autopayOrder = new AutopayOrder();
            autopayOrder.setParkCode(parkCode);
            autopayOrder.setOrderNum(orderNum);
            autopayOrder.setPlateNum(plateNum);
            autopayOrder.setExitTime(exitTime);
            autopayOrder.setServiceStatus(EXIT);
            autopayOrder.setPaidPrice("0");
            autopayOrderDao.update(autopayOrder);
        }
    }

    @Autowired
    private FindCarStatusServiceImpl findCarStatusService;
    private static final int STATUS_EXIT = 0;

    /**
     * 返回是否离场，true为离场状态
     * @param parkCode
     * @param plateNum
     * @param orderNum
     * @return
     * @throws IllegalAccessException
     * @throws IntrospectionException
     * @throws InvocationTargetException
     */
    private Map<String, Object> findCarStatus(String parkCode, String plateNum, String orderNum) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("isExit", false);
        FindCarStatusRequest findCarStatusRequest = new FindCarStatusRequest();
        findCarStatusRequest.setParkCode(parkCode);
        findCarStatusRequest.setPlateNum(plateNum);
        findCarStatusRequest.setOrderNum(orderNum);

        ObjectResponse<FindCarStatusResponse> objectResponse = findCarStatusService.findCarStatus(findCarStatusRequest);
        if (objectResponse != null) {

            if (CodeConstants.SUCCESS.equals(objectResponse.getCode())) {

                FindCarStatusResponse findCarStatusResponse = objectResponse.getData();
                Integer status = findCarStatusResponse.getStatus();
                if (STATUS_EXIT == status){
                    resMap.put("isExit", true);
                    resMap.put("exitTime", findCarStatusResponse.getExitTime());
                }
            }
        }
        return resMap;
    }
}
