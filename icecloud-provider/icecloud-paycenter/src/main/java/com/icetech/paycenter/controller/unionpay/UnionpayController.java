package com.icetech.paycenter.controller.unionpay;

import com.icetech.common.DataChangeTools;
import com.icetech.common.ResultTools;
import com.icetech.common.ToolsUtil;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.exception.ResponseBodyException;
import com.icetech.paycenter.common.tool.UnionpaySignTools;
import com.icetech.paycenter.domain.autopay.ParkUnionpay;
import com.icetech.paycenter.domain.autopay.request.UnionpayBaseNotifyRequest;
import com.icetech.paycenter.domain.autopay.response.UnionpayResponse;
import com.icetech.paycenter.mapper.autopay.ParkUnionpayDao;
import com.icetech.paycenter.service.BaseUnionpayService;
import com.icetech.paycenter.service.autopay.UnionpayNotifyService;
import com.icetech.paycenter.service.factory.UnionpayNotifyRequestFactory;
import com.icetech.paycenter.service.factory.UnionpayNotifyResponseFactory;
import com.icetech.paycenter.service.factory.UnionpayNotifyServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UnionpayController {
    @Autowired
    private ParkUnionpayDao parkUnionpayDao;

    @PostMapping("/unionpay")
    public String parkUnionpay(@RequestParam Map<String, Object> requestMap) {

        System.out.println("银联请求参数：" + requestMap);
        //业务码
        String transId = (String) requestMap.get("TransId");
        String merCode = (String) requestMap.get("MerCode");

        if (ToolsUtil.isNotNull(transId) && ToolsUtil.isNotNull(merCode)) {
            ParkUnionpay parkUnionpay = new ParkUnionpay();
            parkUnionpay.setMerCode(merCode);
            parkUnionpay = parkUnionpayDao.selectById(parkUnionpay);

            UnionpayResponse unionpayResponse = UnionpayNotifyResponseFactory.createResponse(transId);
            unionpayResponse.setRemark((String) requestMap.get("Remark"));

            //签名验证
            boolean signResult = UnionpaySignTools.verifySign(requestMap, parkUnionpay.getSecretKey());
            if (signResult) {
                UnionpayNotifyService unionpayNotifyService = UnionpayNotifyServiceFactory.createBean(transId);
                UnionpayBaseNotifyRequest unionpayBaseNotifyRequest = UnionpayNotifyRequestFactory.createRequest(transId, requestMap);
                try {
                    //调用业务实现类，处理请求
                    return unionpayNotifyService.dealNotify(unionpayBaseNotifyRequest);
                } catch (ResponseBodyException e) {
                    e.printStackTrace();
                    unionpayResponse.setResultCode(BaseUnionpayService.ErrCode.报文格式错误.getCode());
                    unionpayResponse.setResultMsg(BaseUnionpayService.ErrCode.报文格式错误.toString());
                    return DataChangeTools.bean2gson(unionpayResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                    unionpayResponse.setResultCode(BaseUnionpayService.ErrCode.其他异常错误.getCode());
                    unionpayResponse.setResultMsg(BaseUnionpayService.ErrCode.其他异常错误.toString());
                    return DataChangeTools.bean2gson(unionpayResponse);
                }
            } else {
                unionpayResponse.setResultCode(BaseUnionpayService.ErrCode.验签失败.getCode());
                unionpayResponse.setResultMsg(BaseUnionpayService.ErrCode.验签失败.toString());
                return DataChangeTools.bean2gson(unionpayResponse);
            }
        } else {
            return ResultTools.setResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));
        }
    }
}
