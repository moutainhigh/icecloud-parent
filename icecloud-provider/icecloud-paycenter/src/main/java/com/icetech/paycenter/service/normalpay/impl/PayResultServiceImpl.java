package com.icetech.paycenter.service.normalpay.impl;

import com.icetech.common.DataChangeTools;
import com.icetech.common.ResultTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.validator.Validator;
import com.icetech.paycenter.domain.AccountRecord;
import com.icetech.paycenter.domain.request.PayCenterBaseRequest;
import com.icetech.paycenter.domain.request.PayResultRequest;
import com.icetech.paycenter.mapper.AccountRecordDao;
import com.icetech.paycenter.service.IApiService;
import com.icetech.paycenter.service.IPayCenterService;
import com.icetech.paycenter.service.PayCenterServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

import static com.icetech.paycenter.common.tool.BeanNameTools.getBeanName;

/**
 * 支付结果查询
 * @author wangzw
 */
@Service
public class PayResultServiceImpl implements IApiService {
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
        PayResultRequest payResultRequest = DataChangeTools.convert2bean(baseRequest.getBizContent(), PayResultRequest.class);
        if (!Validator.validate(payResultRequest)){
            return ResultTools.setResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));
        }
        AccountRecord accountRecord = accountRecordDao.selectByParkCodeAndTradeNo(payResultRequest.getParkCode(), payResultRequest.getTradeNo());
        if (Objects.isNull(accountRecord)){
            return ResultTools.setResponse(CodeConstants.ERROR_404, CodeConstants.getName(CodeConstants.ERROR_404));
        }
        String beanName = getBeanName(accountRecord.getTradeType());
        if (Objects.isNull(beanName)){
            return ResultTools.setResponse(CodeConstants.ERROR_402, CodeConstants.getName(CodeConstants.ERROR_402)+"下单类型不正确");
        }
        IPayCenterService service = strategyFactory.getPayServiceImpl(beanName);
        return service.doPayResult(payResultRequest);
    }
}
