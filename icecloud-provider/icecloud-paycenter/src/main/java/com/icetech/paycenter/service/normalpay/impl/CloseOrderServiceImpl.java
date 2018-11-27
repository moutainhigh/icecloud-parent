package com.icetech.paycenter.service.normalpay.impl;

import com.icetech.common.DataChangeTools;
import com.icetech.common.ResultTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.validator.Validator;
import com.icetech.paycenter.common.enumeration.PayCenterTradeStatus;
import com.icetech.paycenter.domain.AccountRecord;
import com.icetech.paycenter.domain.request.CloseOrderRequest;
import com.icetech.paycenter.domain.request.PayCenterBaseRequest;
import com.icetech.paycenter.mapper.AccountRecordDao;
import com.icetech.paycenter.service.IApiService;
import com.icetech.paycenter.service.IWxPayCenterService;
import com.icetech.paycenter.service.PayCenterServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

/**
 * 支付结果查询
 * @author wangzw
 */
@Service
public class CloseOrderServiceImpl implements IApiService {
    private static final String WX_BEAN_NAME = "payCenter4WxServiceImpl";
    @Resource
    private PayCenterServiceFactory strategyFactory;
    @Autowired
    private AccountRecordDao accountRecordDao;
    @Override
    public String execute(PayCenterBaseRequest baseRequest) throws Exception {
        //校验基本参数
        if (!Validator.validate(baseRequest)){
            return ResultTools.setResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));
        }
        //解析求求参数
        CloseOrderRequest closeOrderRequest = DataChangeTools.convert2bean(baseRequest.getBizContent(), CloseOrderRequest.class);
        if (!Validator.validate(closeOrderRequest)){
            return ResultTools.setResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));
        }
        AccountRecord accountRecord = accountRecordDao.selectByParkCodeAndTradeNo(closeOrderRequest.getParkCode(), closeOrderRequest.getTradeNo());
        if (Objects.isNull(accountRecord)){
            return ResultTools.setResponse(CodeConstants.ERROR_404, CodeConstants.getName(CodeConstants.ERROR_404));
        }
        //根据不同的支付类型跳转不同的实现类
        String result = "";
        if (accountRecord.getTradeType().startsWith("WX-")){
            IWxPayCenterService wxPayCenterService = (IWxPayCenterService) strategyFactory.getPayServiceImpl(WX_BEAN_NAME);
            result = wxPayCenterService.doCloseOrder(closeOrderRequest);
        }
        if (ResultTools.isSuccess(result)){
            //更新订单状态为关闭
            accountRecord.setStatus(PayCenterTradeStatus.CANCEL.getCode());
            accountRecord.setUpdateTime(new Date());
            accountRecordDao.update(accountRecord);
        }
        return result;
    }
}