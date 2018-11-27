package com.icetech.datacenter.service;

import com.icetech.common.constants.CodeConstants;
import com.icetech.common.exception.ResponseBodyException;
import com.icetech.common.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractService {
    // 日志管理
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 验证参数
     * @param instance
     * @return
     */
    protected void verifyParams(Object instance){
        try {
            if (!Validator.validate(instance)){
                throw new ResponseBodyException(CodeConstants.ERROR_400, "检验参数未通过");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseBodyException(CodeConstants.ERROR_400, "检验参数未通过");
        }
    }
}
