package com.icetech.paycenter.service.autopay.impl;

import com.icetech.common.AssertTools;
import com.icetech.common.DateTools;
import com.icetech.common.ResultTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.validator.Validator;
import com.icetech.paycenter.common.enumeration.PayCenterTradeStatus;
import com.icetech.paycenter.domain.AccountRecord;
import com.icetech.paycenter.domain.request.RefundRequest;
import com.icetech.paycenter.mapper.AccountRecordDao;
import com.icetech.paycenter.service.autopay.IRefundService;
import com.icetech.paycenter.service.impl.PayCenter4UnionServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 银联无感支付-退款
 *
 * @author fangct
 */
@Service
public class AutopayRefundServiceImpl implements IRefundService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PayCenter4UnionServiceImpl unionPayCenterService;

    @Autowired
    private AccountRecordDao accountRecordDao;

    public String refund(RefundRequest refundRequest) throws NoSuchFieldException, IllegalAccessException {

        /**
         * 参数校验
         */
        if (Validator.validate(refundRequest)) {

            String parkCode = refundRequest.getParkCode();
            String tradeNo = refundRequest.getTradeNo();

            AccountRecord accountRecord_para = new AccountRecord();
            accountRecord_para.setParkCode(parkCode);
            accountRecord_para.setTradeNo(tradeNo);
            AccountRecord accountRecord = accountRecordDao.selectById(accountRecord_para);

            AssertTools.notNull(accountRecord, CodeConstants.ERROR_402, "交易不存在");

            //交易成功时才能退款
            if (accountRecord.getStatus().equals(PayCenterTradeStatus.SUCCESS.getCode())) {
                //获取支付时的交易单号
                String outTradeNo = accountRecord.getOutTradeNo();
                String merDate = DateTools.getFormat(DateTools.DF_L,accountRecord.getTradeDate());

                //请求银联退款
                return unionPayCenterService.refund(refundRequest, outTradeNo, merDate);
            } else {
                //退款时状态不合法
                logger.info("<免密支付-退款> 退款时状态不合法，parkCode：{}，tradeNo：{}", parkCode, tradeNo);
                return ResultTools.setResponse(CodeConstants.ERROR_2003, CodeConstants.getName(CodeConstants.ERROR_2003));
            }
        } else {
            return ResultTools.setResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));
        }
    }

}
