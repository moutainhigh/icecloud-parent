package com.icetech.paycenter.service.normalpay.impl;

import com.icetech.common.DataChangeTools;
import com.icetech.common.ResultTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.validator.Validator;
import com.icetech.paycenter.domain.normalpay.request.TransactionDetailsDownloadRequest;
import com.icetech.paycenter.domain.request.PayCenterBaseRequest;
import com.icetech.paycenter.service.IApiService;
import com.icetech.paycenter.service.IPayCenterService;
import com.icetech.paycenter.service.PayCenterServiceFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 交易单明细服务类下载
 * @author wangzw
 */
@Service
public class PaymentDownloadServiceImpl implements IApiService {
    private static String CMBC_BEAN_NAME = "payCenter4CmbcServiceImpl";
    @Resource
    private PayCenterServiceFactory strategyFactory;
    @Override
    public String execute(PayCenterBaseRequest baseRequest) throws Exception {
        //校验基本参数
        if (!Validator.validate(baseRequest)){
            return ResultTools.setResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));
        }
        //解析求求参数
        TransactionDetailsDownloadRequest downloadRequest = DataChangeTools.convert2bean(baseRequest.getBizContent(), TransactionDetailsDownloadRequest.class);
        if (!Validator.validate(downloadRequest)){
            return ResultTools.setResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));
        }
        // TODO 当前只支持民生的交易明细
        IPayCenterService service = strategyFactory.getPayServiceImpl(CMBC_BEAN_NAME);
        return service.downloadTransactionDetails(downloadRequest);
    }
}
