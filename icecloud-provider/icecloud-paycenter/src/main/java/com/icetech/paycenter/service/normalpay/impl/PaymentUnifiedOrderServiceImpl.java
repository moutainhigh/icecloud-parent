package com.icetech.paycenter.service.normalpay.impl;


import com.icetech.common.DataChangeTools;
import com.icetech.common.ResultTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.validator.Validator;
import com.icetech.paycenter.common.enumeration.PayCenterTradeStatus;
import com.icetech.paycenter.common.enumeration.PayScene;
import com.icetech.paycenter.domain.AccountRecord;
import com.icetech.paycenter.domain.normalpay.request.UnifiedOrderRequest;
import com.icetech.paycenter.domain.request.PayCenterBaseRequest;
import com.icetech.paycenter.mapper.AccountRecordDao;
import com.icetech.paycenter.service.IApiService;
import com.icetech.paycenter.service.IPayCenterService;
import com.icetech.paycenter.service.PayCenterServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

import static com.icetech.paycenter.common.tool.BeanNameTools.getBeanName;


/**
 * 下单接口实现类
 * @author wangzw
 */
@Service
public class PaymentUnifiedOrderServiceImpl implements IApiService {

    @Resource
    private PayCenterServiceFactory strategyFactory;
    @Autowired
    private AccountRecordDao accountRecordDao;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String execute(PayCenterBaseRequest baseRequest) throws Exception {
        //校验基本参数
        if (!Validator.validate(baseRequest)){
            return ResultTools.setResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));
        }
        //解析求求参数
        UnifiedOrderRequest unifiedOrderRequest = DataChangeTools.convert2bean(baseRequest.getBizContent(), UnifiedOrderRequest.class);
        if (!Validator.validate(unifiedOrderRequest)){
            return ResultTools.setResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));
        }
        //查询流水记录是否存在
        AccountRecord accountRecord = accountRecordDao.selectByParkCodeAndTradeNo(unifiedOrderRequest.getParkCode(), unifiedOrderRequest.getTradeNo());
        if (Objects.nonNull(accountRecord)){
            return ResultTools.setResponse(CodeConstants.ERROR_405, CodeConstants.getName(CodeConstants.ERROR_405));
        }
        insertAccountRecord(unifiedOrderRequest);
        String beanName = getBeanName(unifiedOrderRequest.getSelectTradeType());
        if (Objects.isNull(beanName)){
            return ResultTools.setResponse(CodeConstants.ERROR_402, CodeConstants.getName(CodeConstants.ERROR_402)+"下单类型不正确");
        }
        IPayCenterService payCenterServiceImpl = strategyFactory.getPayServiceImpl(beanName);
        return payCenterServiceImpl.doUnifiedOrder(unifiedOrderRequest);
    }

    private void insertAccountRecord(UnifiedOrderRequest unifiedOrderRequest) {
        //创建流水记录信息
        AccountRecord accountRecord = new AccountRecord();
        accountRecord.setCreateTime(new Date());
        accountRecord.setTradeDate(new Date());
        accountRecord.setTradeType(unifiedOrderRequest.getSelectTradeType());
        accountRecord.setIncome(Integer.parseInt(unifiedOrderRequest.getPrice()));
        accountRecord.setTradeNo(unifiedOrderRequest.getTradeNo());
        accountRecord.setPayScene(PayScene.主动支付.getValue());
        accountRecord.setParkCode(unifiedOrderRequest.getParkCode());
        accountRecord.setTerminalType(unifiedOrderRequest.getSelectTradeType());
        accountRecord.setOutTradeNo(unifiedOrderRequest.getParkCode()+unifiedOrderRequest.getTradeNo());
        accountRecord.setStatus(PayCenterTradeStatus.WAIT.getCode());
        accountRecord.setOpenId(unifiedOrderRequest.getOpenId());
        accountRecord.setUserId(unifiedOrderRequest.getUserId());
        accountRecord.setCenterInfo(unifiedOrderRequest.getExtraInfo());
        accountRecordDao.insert(accountRecord);
    }
}
