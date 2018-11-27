package com.icetech.paycenter.service.impl;

import com.icetech.common.AssertTools;
import com.icetech.common.DataChangeTools;
import com.icetech.common.ResultTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.validator.Validator;
import com.icetech.paycenter.common.enumeration.PayCenterTradeStatus;
import com.icetech.paycenter.common.enumeration.PayScene;
import com.icetech.paycenter.domain.AccountRecord;
import com.icetech.paycenter.domain.request.PayCenterBaseRequest;
import com.icetech.paycenter.domain.request.RefundRequest;
import com.icetech.paycenter.mapper.AccountRecordDao;
import com.icetech.paycenter.service.IApiService;
import com.icetech.paycenter.service.IPayCenterService;
import com.icetech.paycenter.service.PayCenterServiceFactory;
import com.icetech.paycenter.service.autopay.impl.AutopayRefundServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.icetech.paycenter.common.tool.BeanNameTools.getBeanName;


/**
 * 通用查询订单结果的实现
 * @author fangct
 */
@Service
public class CommonRefundServiceImpl implements IApiService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private AccountRecordDao accountRecordDao;
    @Resource
    private PayCenterServiceFactory strategyFactory;
    @Autowired
    private AutopayRefundServiceImpl autopayRefundService;

    @Override
    public String execute(PayCenterBaseRequest baseRequest) {
        RefundRequest refundRequest = DataChangeTools.convert2bean(baseRequest.getBizContent(), RefundRequest.class);

        /**
         * 参数校验
         */
        try {
            if(Validator.validate(refundRequest)){

                String parkCode = refundRequest.getParkCode();
                String tradeNo = refundRequest.getTradeNo();

                AccountRecord accountRecord_para = new AccountRecord();
                accountRecord_para.setParkCode(parkCode);
                accountRecord_para.setTradeNo(tradeNo);
                AccountRecord accountRecord = accountRecordDao.selectById(accountRecord_para);
                AssertTools.notNull(accountRecord, CodeConstants.ERROR_402, "交易不存在");

                //交易成功的交易才有发起退款
                if (accountRecord.getStatus().equals(PayCenterTradeStatus.SUCCESS.getCode())){
                    //如果退款金额大于支付金额
                    if (accountRecord.getIncome() < Integer.parseInt(refundRequest.getPrice())){
                        return ResultTools.setResponse(CodeConstants.ERROR_2005, CodeConstants.getName(CodeConstants.ERROR_2005));
                    }

                    String result = "";
                    if (accountRecord.getPayScene() == PayScene.免密支付.getValue()){
                        //免密支付时
                        result = autopayRefundService.refund(refundRequest);
                    }else{
                        //主动支付的查询业务
                        String beanName = getBeanName(accountRecord.getTradeType());
                        IPayCenterService service = strategyFactory.getPayServiceImpl(beanName);
                        result = service.doRefund(refundRequest);
                    }
                    if (ResultTools.isSuccess(result)) {
                        AccountRecord accountRecord_update = new AccountRecord();
                        accountRecord_update.setId(accountRecord.getId());
                        accountRecord_update.setStatus(PayCenterTradeStatus.TRANSFER.getCode());
                        accountRecordDao.update(accountRecord_update);
                    }
                    return result;
                }else{
                    return ResultTools.setResponse(CodeConstants.ERROR_2003, CodeConstants.getName(CodeConstants.ERROR_2003));
                }
            }else{
                return ResultTools.setResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultTools.setResponse(CodeConstants.ERROR, CodeConstants.getName(CodeConstants.ERROR));
        }
    }

}
