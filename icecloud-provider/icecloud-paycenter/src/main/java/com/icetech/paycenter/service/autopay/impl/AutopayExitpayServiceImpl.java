package com.icetech.paycenter.service.autopay.impl;

import com.icetech.common.AssertTools;
import com.icetech.common.DataChangeTools;
import com.icetech.common.ResultTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.domain.response.Response;
import com.icetech.common.validator.Validator;

import com.icetech.paycenter.common.enumeration.PayCenterTradeStatus;
import com.icetech.paycenter.domain.AccountRecord;
import com.icetech.paycenter.domain.autopay.AutopayOrder;
import com.icetech.paycenter.domain.autopay.request.ExitpayRequest;
import com.icetech.paycenter.domain.request.PayCenterBaseRequest;
import com.icetech.paycenter.mapper.AccountRecordDao;
import com.icetech.paycenter.mapper.autopay.AutopayOrderDao;
import com.icetech.paycenter.service.IApiService;
import com.icetech.paycenter.service.impl.PayCenter4UnionServiceImpl;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 银联无感支付
 * @author fangct
 */
@Service
public class AutopayExitpayServiceImpl implements IApiService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int AUTO_PAY = 2;

    @Autowired
    private PayCenter4UnionServiceImpl unionPayCenterService;

    @Autowired
    private AccountRecordDao accountRecordDao;
    @Autowired
    private AutopayOrderDao autopayOrderDao;

    @Override
    public String execute(PayCenterBaseRequest baseRequest) throws NoSuchFieldException, IllegalAccessException {
        ExitpayRequest exitpayRequest = DataChangeTools.convert2bean(baseRequest.getBizContent(), ExitpayRequest.class);

        /**
         * 参数校验
         */
        if (Validator.validate(exitpayRequest)) {

            String parkCode = exitpayRequest.getParkCode();
            String tradeNo = exitpayRequest.getTradeNo();
            String plateNum = exitpayRequest.getPlateNum();

            AccountRecord accountRecord = new AccountRecord();
            accountRecord.setParkCode(parkCode);
            accountRecord.setTradeNo(tradeNo);
            accountRecord = accountRecordDao.selectById(accountRecord);
            if (accountRecord == null) {

                AutopayOrder autopayOrder = autopayOrderDao.selectInParkByPlateNum(plateNum);
                AssertTools.notNull(autopayOrder, CodeConstants.ERROR_402, "未找到该车牌的场内记录");

                //如果用户开通了免密支付，则请求扣费，否则返回不支持免密提示
                if (autopayOrder.getIsOpen() == 1) {
                    String outTradeNo = parkCode + tradeNo;
                    String result = unionPayCenterService.exitpay(exitpayRequest, outTradeNo);
                    //组装流水账实体
                    accountRecord = new AccountRecord();
                    accountRecord.setTradeDate(new Date());
                    accountRecord.setTradeType(AutopayType.银联无感支付.getType());
                    accountRecord.setPayScene(AUTO_PAY);
                    accountRecord.setIncome(Integer.parseInt(exitpayRequest.getUnpayPrice())
                    );
                    accountRecord.setTradeNo(exitpayRequest.getTradeNo());
                    accountRecord.setParkCode(exitpayRequest.getParkCode());
                    accountRecord.setTerminalType(AutopayType.银联无感支付.getType());

                    accountRecord.setOutTradeNo(outTradeNo);

                    Response response = DataChangeTools.gson2bean(result, Response.class);
                    if (CodeConstants.SUCCESS.equals(response.getCode())) {
                        accountRecord.setStatus(PayCenterTradeStatus.SUCCESS.getCode());
                    } else {
                        accountRecord.setStatus(PayCenterTradeStatus.ERROR.getCode());
                    }
                    //写入流水账
                    accountRecordDao.insert(accountRecord);
                    //响应结果
                    return result;
                } else {
                    return ResultTools.setResponse(CodeConstants.ERROR_2002, CodeConstants.getName(CodeConstants.ERROR_2002));
                }
            } else {
                //根据车场编号和交易号查询到了重复的流水账
                logger.info("<免密支付-离场扣款> 根据车场编号和交易号查询到了重复的流水账，parkCode：{}，tradeNo：{}", parkCode, tradeNo);
                return ResultTools.setResponse(CodeConstants.ERROR_405, CodeConstants.getName(CodeConstants.ERROR_405));
            }
        } else {
            return ResultTools.setResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));
        }
    }

    public enum AutopayType {

        银联无感支付("AUTOPAY_UNION");

        @Getter
        private String type;
        private AutopayType(String type){
            this.type = type;
        }
    }

}
