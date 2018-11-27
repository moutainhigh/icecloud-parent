package com.icetech.paycenter.service.autopay.impl;

import com.icetech.common.AssertTools;
import com.icetech.common.DataChangeTools;
import com.icetech.common.ResultTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.domain.response.Response;
import com.icetech.common.validator.Validator;
import com.icetech.paycenter.domain.autopay.AutopayOrder;
import com.icetech.paycenter.domain.autopay.request.ExitNotifyRequest;
import com.icetech.paycenter.domain.request.PayCenterBaseRequest;
import com.icetech.paycenter.mapper.autopay.AutopayOrderDao;
import com.icetech.paycenter.service.IApiService;
import com.icetech.paycenter.service.impl.PayCenter4UnionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 银联无感支付
 *
 * @author fangct
 */
@Service
public class AutopayExitNotifyServiceImpl implements IApiService {

    private static final int EXIT = 2;
    private static final int ENTER = 1;

    @Autowired
    private PayCenter4UnionServiceImpl unionPayCenterService;
    @Autowired
    private AutopayOrderDao autopayOrderDao;

    @Override
    public String execute(PayCenterBaseRequest baseRequest) throws Exception {
        ExitNotifyRequest exitNotifyRequest = DataChangeTools.convert2bean(baseRequest.getBizContent(), ExitNotifyRequest.class);

        /**
         * 参数校验
         */
        if (Validator.validate(exitNotifyRequest)) {
            String parkCode = exitNotifyRequest.getParkCode();
            String orderNum = exitNotifyRequest.getOrderNum();

            AutopayOrder autopayOrder = new AutopayOrder();
            autopayOrder.setParkCode(parkCode);
            autopayOrder.setOrderNum(orderNum);
            autopayOrder = autopayOrderDao.selectById(autopayOrder);
            AssertTools.notNull(autopayOrder, CodeConstants.ERROR_402, "未找到车辆场内记录");

            if (autopayOrder.getServiceStatus().equals(ENTER)) {
                String result = unionPayCenterService.exitNotify(exitNotifyRequest);

                Response response = DataChangeTools.gson2bean(result, Response.class);
                if (CodeConstants.SUCCESS.equals(response.getCode())) {
                    //准备无感支付的订单实体
                    autopayOrder = new AutopayOrder();
                    autopayOrder.setParkCode(exitNotifyRequest.getParkCode());
                    autopayOrder.setOrderNum(exitNotifyRequest.getOrderNum());
                    autopayOrder.setPlateNum(exitNotifyRequest.getPlateNum());
                    autopayOrder.setExitTime(exitNotifyRequest.getExitTime());
                    autopayOrder.setServiceStatus(EXIT);
                    autopayOrder.setPaidPrice(exitNotifyRequest.getPaidPrice());
                    autopayOrderDao.update(autopayOrder);
                }
                return result;
            } else {
                //根据车场编号和订单号找到了重复记录
                return ResultTools.setResponse(CodeConstants.ERROR_405, CodeConstants.getName(CodeConstants.ERROR_405));
            }
        } else {
            return ResultTools.setResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));
        }
    }
}
