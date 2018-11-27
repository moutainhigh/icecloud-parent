package com.icetech.paycenter.service.autopay.impl;

import com.icetech.common.AssertTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.paycenter.common.enumeration.PayCenterTradeStatus;
import com.icetech.paycenter.domain.AccountRecord;
import com.icetech.paycenter.domain.request.PayResultRequest;
import com.icetech.paycenter.mapper.AccountRecordDao;
import com.icetech.paycenter.service.IQueryOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 银联无感支付-查询交易结果
 *
 * @author fangct
 */
@Service
public class AutopayQueryOrderServiceImpl implements IQueryOrderService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AccountRecordDao accountRecordDao;

    @Override
    public String queryOrder(PayResultRequest payResultRequest) {

        String parkCode = payResultRequest.getParkCode();
        String tradeNo = payResultRequest.getTradeNo();

        AccountRecord accountRecord_para = new AccountRecord();
        accountRecord_para.setParkCode(parkCode);
        accountRecord_para.setTradeNo(tradeNo);
        AccountRecord accountRecord = accountRecordDao.selectById(accountRecord_para);
        AssertTools.notNull(accountRecord, CodeConstants.ERROR_402, "交易不存在");

        //交易成功时才能退款
        if (accountRecord.getStatus().equals(PayCenterTradeStatus.SUCCESS.getCode())) {
            //响应结果
            return "";
        } else {
            //请求支付平台，查询交易结果
            return "";
        }
    }

}
