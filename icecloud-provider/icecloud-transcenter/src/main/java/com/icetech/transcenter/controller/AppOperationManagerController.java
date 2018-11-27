package com.icetech.transcenter.controller;

import com.icetech.common.DataChangeTools;
import com.icetech.common.ResultTools;
import com.icetech.common.SignTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.exception.ResponseBodyException;
import com.icetech.common.validator.Validator;
import com.icetech.transcenter.domain.ApiBaseRequest;
import com.icetech.transcenter.domain.ThirdInfo;
import com.icetech.transcenter.domain.request.*;
import com.icetech.transcenter.mapper.ThirdInfoDao;
import com.icetech.transcenter.service.app.IAppOperationManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;

/**
 * 运维管理APP调用controller
 * @author wangzw
 */
@RestController
@RequestMapping("/operation")
public class AppOperationManagerController {
    @Autowired
    private IAppOperationManagerService appOperationManagerService;
    @Autowired
    private ThirdInfoDao thirdInfoDao;


    @PostMapping("method")
    public String searchCarInfo(HttpServletRequest request, @RequestBody ApiBaseRequest baseRequest) throws NoSuchFieldException, IllegalAccessException, UnknownHostException {
        //验证签名
//        verifySign(baseRequest);
        //根据server选择不同的方法
        String serviceName = baseRequest.getServiceName();
        return doMethod(serviceName,baseRequest);
    }


    private String doMethod(String serviceName, ApiBaseRequest baseRequest) throws NoSuchFieldException, IllegalAccessException, UnknownHostException {
        String result = null;
        switch (serviceName){
            case ServiceName.SEARCHCAR:{
                //车辆信息查询
                AppSearchCarRequest request = DataChangeTools.convert2bean(baseRequest.getBizContent(), AppSearchCarRequest.class);
                if (!Validator.validate(request)) return ResultTools.setResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));
                result = appOperationManagerService.searchCar(request);
                break;
            }
            case ServiceName.QUERYFEE:{
                //拉去费用
                AppPullFeeRequest request = DataChangeTools.convert2bean(baseRequest.getBizContent(), AppPullFeeRequest.class);
                if (!Validator.validate(request)) return ResultTools.setResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));
                result = appOperationManagerService.queryFee(request);
                break;
            }
            case ServiceName.QRCODE:{
                //下单获取二维码
                AppUnifiedOrderRequest request = DataChangeTools.convert2bean(baseRequest.getBizContent(), AppUnifiedOrderRequest.class);
                if (!Validator.validate(request)) return ResultTools.setResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));
                result = appOperationManagerService.doUnifiedOrder(request);
                break;
            }
            case ServiceName.QUERYPAYRESULT:{
                //结果查询
                AppPayResultRequest request = DataChangeTools.convert2bean(baseRequest.getBizContent(), AppPayResultRequest.class);
                if (!Validator.validate(request)) return ResultTools.setResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));
                result = appOperationManagerService.queryPayResult(request);
                break;
            }
            case ServiceName.NOTIFYPREPAY:{
                //预交费通知
                AppPrePaymentNotifyRequest request = DataChangeTools.convert2bean(baseRequest.getBizContent(), AppPrePaymentNotifyRequest.class);
                if (!Validator.validate(request)) return ResultTools.setResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));
                result = appOperationManagerService.notifyPrepay(request);
                break;
            }
        }
        return result;
    }

    /**
     * 验证签名
     * @param baseRequest
     */
    private void verifySign(ApiBaseRequest baseRequest) throws NoSuchFieldException, IllegalAccessException {
        if (Validator.validate(baseRequest)){
            String pid = baseRequest.getPid();
            ThirdInfo thirdInfo = new ThirdInfo();
            thirdInfo.setPid(pid);
            thirdInfo = thirdInfoDao.selectById(thirdInfo);
            String secretKey = thirdInfo.getSecretKey();
            boolean verifyResult = SignTools.verifyMD5Sign(baseRequest, secretKey);
            if (!verifyResult) {
                throw new ResponseBodyException(CodeConstants.ERROR_401, "验签未通过");
            }
        }else{
            throw new ResponseBodyException(CodeConstants.ERROR_400, "必填参数为空");
        }
    }

    static class ServiceName{
        private  final static String SEARCHCAR= "searchCar";
        private  final static String QUERYFEE= "queryFee";
        private  final static String QRCODE= "QRCode";
        private  final static String QUERYPAYRESULT= "queryPayResult";
        private  final static String NOTIFYPREPAY= "notifyPrepay";
    }
}
