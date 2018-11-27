package com.icetech.paycenter.service.autopay.impl;

import com.icetech.common.DataChangeTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.validator.Validator;
import com.icetech.paycenter.domain.autopay.request.UnionpayBaseNotifyRequest;
import com.icetech.paycenter.domain.autopay.request.UnionpaySignedNotifyRequest;
import com.icetech.paycenter.domain.autopay.response.UnionpayFindCarStatusResponse;
import com.icetech.paycenter.mapper.autopay.AutopayOrderDao;
import com.icetech.paycenter.service.BaseUnionpayService;
import com.icetech.paycenter.service.autopay.UnionpayNotifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 银联无感支付-场内签约推送
 * @author fangct
 */
@Service
public class AutopaySignedNotifyServiceImpl implements UnionpayNotifyService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String TRANS_ID = "40702";

    @Autowired
    private AutopayOrderDao autopayOrderDao;

    @Override
    public String dealNotify(UnionpayBaseNotifyRequest baseRequest) throws Exception {

        UnionpaySignedNotifyRequest unionpaySignedNotifyRequest = (UnionpaySignedNotifyRequest) baseRequest;

        //响应对象
        UnionpayFindCarStatusResponse unionpayFindCarStatusResponse = new UnionpayFindCarStatusResponse();
        unionpayFindCarStatusResponse.setTransId(TRANS_ID);
        unionpayFindCarStatusResponse.setRemark(baseRequest.getRemark());

        //验证参数
        if (Validator.validate(unionpaySignedNotifyRequest)) {
            String plateNum = unionpaySignedNotifyRequest.getCarNo();

            //如果更新成功，则返回成功
            try{
                //同步开启在场内车牌的免密功能
                autopayOrderDao.updateOpenStatusByPlateNum(plateNum);
                unionpayFindCarStatusResponse.setResultCode(BaseUnionpayService.ErrCode.成功.getCode());
            }catch (Exception e){
                logger.error("<场内签约推送接口> 开启免密支付状态失败，车牌号：{}，异常：{}", plateNum, e);
                unionpayFindCarStatusResponse.setResultCode(BaseUnionpayService.ErrCode.其他异常错误.getCode());
                unionpayFindCarStatusResponse.setResultMsg(CodeConstants.getName(CodeConstants.ERROR));
            }
        }else{
            unionpayFindCarStatusResponse.setResultCode(BaseUnionpayService.ErrCode.报文格式错误.getCode());
            unionpayFindCarStatusResponse.setResultMsg(BaseUnionpayService.ErrCode.报文格式错误.toString());
        }
        return DataChangeTools.bean2gson(unionpayFindCarStatusResponse);
    }

}
