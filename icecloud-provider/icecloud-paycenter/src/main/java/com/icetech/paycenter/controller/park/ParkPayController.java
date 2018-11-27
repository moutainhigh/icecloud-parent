package com.icetech.paycenter.controller.park;

import com.icetech.common.SignTools;
import com.icetech.common.ToolsUtil;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.exception.ResponseBodyException;
import com.icetech.common.validator.Validator;
import com.icetech.paycenter.domain.ThirdInfo;
import com.icetech.paycenter.domain.request.PayCenterBaseRequest;
import com.icetech.paycenter.mapper.ThirdInfoDao;
import com.icetech.paycenter.service.IApiService;
import com.icetech.paycenter.service.Spring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 停车场支付请求处理
 * @author fangct
 */
@RestController
public class ParkPayController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String SERVICE_SUBFIX = "ServiceImpl";

    @Autowired
    private ThirdInfoDao thirdInfoDao;

    @PostMapping("/parkpay")
    public String parkpay(@RequestBody PayCenterBaseRequest baseRequest) throws Exception {

        //签名校验
        verifySign(baseRequest);

        String serviceName = baseRequest.getServiceName();
        IApiService apiService = getBean(serviceName);
        return apiService.execute(baseRequest);
    }

    /**
     * 获取service bean
     * @param serviceName
     * @return
     */
    private IApiService getBean(String serviceName){
        String beanNameProfile = "";
        if (serviceName.contains(".")){
            String[] arr = serviceName.split("\\.");
            beanNameProfile = arr[0];
            for (int i = 1; i<arr.length;i++){
                beanNameProfile += ToolsUtil.toUpperCaseFirstOne(arr[i]);
            }
        }else{
            beanNameProfile = serviceName;
        }
        try {
            return Spring.getBean(beanNameProfile + SERVICE_SUBFIX);
        }catch (Exception e){
            logger.error("未找到对应的serviceName：{}", serviceName);
            throw new ResponseBodyException(CodeConstants.ERROR_404, "未找到服务接口实现");
        }
    }

    /**
     * 验证签名
     * @param baseRequest
     */
    private void verifySign(PayCenterBaseRequest baseRequest) throws NoSuchFieldException, IllegalAccessException {
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
}
