package com.icetech.paycenter.rpc;

import com.icetech.api.paycenter.model.request.*;
import com.icetech.api.paycenter.model.response.PayResultResponseDto;
import com.icetech.api.paycenter.model.response.UnifiedOrderResponseDto;
import com.icetech.api.paycenter.service.PayCenterFeignApi;
import com.icetech.common.DataChangeTools;
import com.icetech.common.ResultTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.util.ResponseUtils;
import com.icetech.paycenter.common.enumeration.PayCenterTradeStatus;
import com.icetech.paycenter.common.enumeration.PayScene;
import com.icetech.paycenter.common.enumeration.SelectTradeType;
import com.icetech.paycenter.domain.AccountRecord;
import com.icetech.paycenter.domain.autopay.request.EnterNotifyRequest;
import com.icetech.paycenter.domain.autopay.request.ExitNotifyRequest;
import com.icetech.paycenter.domain.autopay.request.ExitpayRequest;
import com.icetech.paycenter.domain.normalpay.request.UnifiedOrderRequest;
import com.icetech.paycenter.domain.normalpay.response.UnifiedOrderResponse;
import com.icetech.paycenter.domain.request.PayCenterBaseRequest;
import com.icetech.paycenter.domain.request.PayResultRequest;
import com.icetech.paycenter.mapper.AccountRecordDao;
import com.icetech.paycenter.service.IAutopayService;
import com.icetech.paycenter.service.PayCenterServiceFactory;
import com.icetech.paycenter.service.autopay.impl.AutopayExitpayServiceImpl;
import com.icetech.paycenter.service.impl.PayCenter4AliServiceImpl;
import com.icetech.paycenter.service.impl.PayCenter4WxServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Objects;

import static com.icetech.paycenter.common.tool.BeanNameTools.getBeanName;

@RestController
public class PayCenterFeignClient implements PayCenterFeignApi {
    private static final Logger logger = LoggerFactory.getLogger(PayCenterFeignClient.class);

    @Autowired
    private PayCenterServiceFactory payCenterServiceFactory;
    @Autowired
    private AccountRecordDao accountRecordDao;
    @Autowired
    private PayCenter4WxServiceImpl payCenter4WxService;
    @Autowired
    private PayCenter4AliServiceImpl payCenter4AliService;
    @Autowired
    private AutopayExitpayServiceImpl autopayExitpayService;
    @Autowired
    private IAutopayService autopayService;

    @Override
    public ObjectResponse<UnifiedOrderResponseDto> doUnifiedOrder(UnifiedOrderRequestDto unifiedOrderRequestDto) {
        try {
            logger.info("【RPC 调用下单开始】");
            UnifiedOrderRequest unifiedOrderRequest = new UnifiedOrderRequest();
            BeanUtils.copyProperties(unifiedOrderRequestDto, unifiedOrderRequest);
            //查询流水记录是否存在
            AccountRecord accountRecord = accountRecordDao.selectByParkCodeAndTradeNo(unifiedOrderRequest.getParkCode(), unifiedOrderRequest.getTradeNo());
            if (Objects.nonNull(accountRecord)){
                return new ObjectResponse(CodeConstants.ERROR_405, CodeConstants.getName(CodeConstants.ERROR_405));
            }
            insertAccountRecord(unifiedOrderRequest);
            String beanName = getBeanName(unifiedOrderRequest.getSelectTradeType());
            if (Objects.isNull(beanName)){
                return new ObjectResponse(CodeConstants.ERROR_402, CodeConstants.getName(CodeConstants.ERROR_402)+"下单类型不正确");
            }
            com.icetech.paycenter.service.IPayCenterService payServiceImpl = payCenterServiceFactory.getPayServiceImpl(beanName);
            String response = payServiceImpl.doUnifiedOrder(unifiedOrderRequest);
            ObjectResponse<UnifiedOrderResponseDto> objectResponse = ResultTools.getObjectResponse(response, UnifiedOrderResponse.class);
            return objectResponse;
        }catch (Exception e){
            logger.error("【RPC下单异常】>>>>异常信息：{}",e.getMessage());
            return new ObjectResponse(CodeConstants.ERROR, CodeConstants.getName(CodeConstants.ERROR),e.getMessage());
        }

    }


    @Override
    public ObjectResponse autoPay(ExitpayRequestDto exitpayRequestDto) {
        PayCenterBaseRequest<ExitpayRequest> baseRequest = new PayCenterBaseRequest<>();

        try {
            String res = autopayExitpayService.execute(baseRequest);
            return DataChangeTools.gson2bean(res, ObjectResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtils.returnErrorResponse(CodeConstants.ERROR);
        }
    }



    @Override
    public ObjectResponse<PayResultResponseDto> getPayResult(PayResultRequestDto payResultRequestDto, String tradeType) {
        PayResultRequest payResultRequest = new PayResultRequest();
        BeanUtils.copyProperties(payResultRequestDto, payResultRequest);
        AccountRecord accountRecord = accountRecordDao.selectByParkCodeAndTradeNo(payResultRequest.getParkCode(), payResultRequest.getTradeNo());
        if (Objects.isNull(accountRecord)){
            return new ObjectResponse(CodeConstants.ERROR_404, CodeConstants.getName(CodeConstants.ERROR_404));
        }
        String result = null;
        //微信结果查询
        if (tradeType.equals(SelectTradeType.WX_NATIVE.getCode())){
            result = payCenter4WxService.doPayResult(payResultRequest);
        }
        //支付宝结果查询
        if (tradeType.equals(SelectTradeType.ALI_QR.getCode())){
            result = payCenter4AliService.doPayResult(payResultRequest);
        }
        ObjectResponse objectResponse = ResultTools.getObjectResponse(result, PayResultResponseDto.class);
        return objectResponse;
    }

    @Override
    public ObjectResponse<String> autoPayEnterNotify(EnterNotifyRequestDto enterNotifyRequestDto) {
        EnterNotifyRequest enterNotifyRequest = new EnterNotifyRequest();
        BeanUtils.copyProperties(enterNotifyRequestDto, enterNotifyRequest);
        String result = autopayService.enterNotify(enterNotifyRequest);
        ObjectResponse<String> objectResponse = ResultTools.getObjectResponse(result, String.class);
        return objectResponse;
    }

    @Override
    public ObjectResponse<String> autoPayExitNotify(ExitNotifyRequestDto exitNotifyRequestDto) {
        ExitNotifyRequest exitNotifyRequest = new ExitNotifyRequest();
        BeanUtils.copyProperties(exitNotifyRequestDto, exitNotifyRequest);
        String result = autopayService.exitNotify(exitNotifyRequest);
        ObjectResponse<String> objectResponse = ResultTools.getObjectResponse(result, String.class);
        return objectResponse;
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
