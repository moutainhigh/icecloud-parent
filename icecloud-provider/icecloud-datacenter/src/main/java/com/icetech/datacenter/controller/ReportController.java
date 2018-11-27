package com.icetech.datacenter.controller;

import com.icetech.api.cloudcenter.service.ParkFeignApi;
import com.icetech.common.ResultTools;
import com.icetech.common.SignTools;
import com.icetech.common.ToolsUtil;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.domain.Park;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.exception.ResponseBodyException;
import com.icetech.common.util.ResponseUtils;
import com.icetech.common.validator.Validator;
import com.icetech.datacenter.domain.request.DataCenterBaseRequest;
import com.icetech.datacenter.service.Spring;
import com.icetech.datacenter.service.report.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ReportController {
    private Logger logger = LoggerFactory.getLogger(ReportController.class);

    //service实现bean名的后缀
    private final String SERVICE_SUFFIX = "ServiceImpl";

    @Autowired
    private ParkFeignApi parkService;

    @PostMapping("/report")
    public String executeReport(@RequestBody DataCenterBaseRequest baseRequest, HttpServletRequest request) throws Exception {
        //放入当前时间
        request.setAttribute("startTime", System.currentTimeMillis());
        String parkCode = baseRequest.getParkCode();
        if (ToolsUtil.isNull(parkCode)){
            logger.info("车场编号为空");
            return ResultTools.setResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));
        }

        ObjectResponse<Park> objectResponse = parkService.findByParkCode(baseRequest.getParkCode());
        ResponseUtils.notError(objectResponse);
        Park park = objectResponse.getData();
        //签名校验
//        verify(baseRequest, park.getKey());

        String prefix = baseRequest.getServiceName();
        ReportService reportService = getServiceBean(prefix);
        return reportService.report(baseRequest, park.getId());

    }

    /**
     * 获取service实现类
     * @param prefix
     * @return
     */
    private ReportService getServiceBean(String prefix){
        String serviceName = prefix + SERVICE_SUFFIX;
        try {
            return Spring.getBean(serviceName);
        }catch(RuntimeException e){
            throw new ResponseBodyException(CodeConstants.ERROR_404, CodeConstants.getName(CodeConstants.ERROR_404));
        }
    }

    /**
     * 验证参数和签名
     * @param baseRequest
     * @param secretKey
     */
    private void verify(DataCenterBaseRequest baseRequest, String secretKey) {
        try {
            if (Validator.validate(baseRequest)){

                boolean verifyResult = SignTools.verifyMD5Sign(baseRequest, secretKey);
                if (!verifyResult) {
                    throw new ResponseBodyException(CodeConstants.ERROR_401, "验签未通过");
                }
            }else{
                throw new ResponseBodyException(CodeConstants.ERROR_400, "必填参数为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseBodyException(CodeConstants.ERROR, "");
        }
    }
}
