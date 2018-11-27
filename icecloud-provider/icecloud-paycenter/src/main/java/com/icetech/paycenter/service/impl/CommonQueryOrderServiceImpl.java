package com.icetech.paycenter.service.impl;

import com.icetech.common.AssertTools;
import com.icetech.common.DataChangeTools;
import com.icetech.common.ResultTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.validator.Validator;
import com.icetech.paycenter.common.enumeration.PayCenterTradeStatus;
import com.icetech.paycenter.common.enumeration.PayScene;
import com.icetech.paycenter.common.enumeration.ResponseTradeStatus;
import com.icetech.paycenter.domain.AccountRecord;
import com.icetech.paycenter.domain.normalpay.response.PayResultResponse;
import com.icetech.paycenter.domain.request.PayCenterBaseRequest;
import com.icetech.paycenter.domain.request.PayResultRequest;
import com.icetech.paycenter.mapper.AccountRecordDao;
import com.icetech.paycenter.service.IApiService;
import com.icetech.paycenter.service.IPayCenterService;
import com.icetech.paycenter.service.PayCenterServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.icetech.paycenter.common.tool.BeanNameTools.getBeanName;


/**
 * 通用查询订单结果的实现
 *
 * @author fangct
 */
@Service
public class CommonQueryOrderServiceImpl implements IApiService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private AccountRecordDao accountRecordDao;
    @Resource
    private PayCenterServiceFactory strategyFactory;

    @Override
    public String execute(PayCenterBaseRequest baseRequest) throws NoSuchFieldException, IllegalAccessException {
        PayResultRequest payResultRequest = DataChangeTools.convert2bean(baseRequest.getBizContent(), PayResultRequest.class);

        /**
         * 参数校验
         */
        if (Validator.validate(payResultRequest)) {

            String parkCode = payResultRequest.getParkCode();
            String tradeNo = payResultRequest.getTradeNo();

            AccountRecord accountRecord_para = new AccountRecord();
            accountRecord_para.setParkCode(parkCode);
            accountRecord_para.setTradeNo(tradeNo);
            AccountRecord accountRecord = accountRecordDao.selectById(accountRecord_para);
            AssertTools.notNull(accountRecord, CodeConstants.ERROR_402, "交易不存在");
            //交易中时：发起request查询请求，否则以流水账表中状态返回
            if (accountRecord.getStatus().equals(PayCenterTradeStatus.WAIT.getCode())) {
                //获取支付时的交易单号和日期
                if (accountRecord.getPayScene() == PayScene.免密支付.getValue()) {
                    return buildResponse(accountRecord);
                } else {
                    //主动支付的查询业务
                    String beanName = getBeanName(accountRecord.getTradeType());
                    IPayCenterService service = strategyFactory.getPayServiceImpl(beanName);
                    return service.doPayResult(payResultRequest);
                }
            } else {
                return buildResponse(accountRecord);
            }
        } else {
            return ResultTools.setResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));
        }
    }

    /**
     * 用流水账表中的状态构建响应结果
     *
     * @param accountRecord
     * @return
     */
    private String buildResponse(AccountRecord accountRecord) {
        PayResultResponse payResultResponse = new PayResultResponse();
        payResultResponse.setPrice(accountRecord.getIncome().toString());
        String status = accountRecord.getStatus();
        if (status.equals(PayCenterTradeStatus.SUCCESS.getCode())) {
            payResultResponse.setTradeStatus(ResponseTradeStatus.订单交易成功.getCode());
        } else if (status.equals(PayCenterTradeStatus.ERROR.getCode())) {
            payResultResponse.setTradeStatus(ResponseTradeStatus.下单失败.getCode());
        } else if (status.equals(PayCenterTradeStatus.CANCEL.getCode())) {
            payResultResponse.setTradeStatus(ResponseTradeStatus.已撤销已关闭.getCode());
        } else if (status.equals(PayCenterTradeStatus.TRANSFER.getCode())) {
            payResultResponse.setTradeStatus(ResponseTradeStatus.订单转入退款.getCode());
        } else if (status.equals(PayCenterTradeStatus.WAIT.getCode())) {
            payResultResponse.setTradeStatus(ResponseTradeStatus.未支付待支付.getCode());
        }
        return ResultTools.setResponse(CodeConstants.SUCCESS, CodeConstants.getName(CodeConstants.SUCCESS), payResultResponse);
    }
}
