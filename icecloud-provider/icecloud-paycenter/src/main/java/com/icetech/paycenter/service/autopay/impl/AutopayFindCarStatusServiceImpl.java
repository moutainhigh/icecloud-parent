package com.icetech.paycenter.service.autopay.impl;

import com.icetech.common.AssertTools;
import com.icetech.common.DataChangeTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.validator.Validator;
import com.icetech.paycenter.domain.autopay.ParkUnionpay;
import com.icetech.paycenter.domain.autopay.request.FindCarStatusRequest;
import com.icetech.paycenter.domain.autopay.request.UnionpayBaseNotifyRequest;
import com.icetech.paycenter.domain.autopay.request.UnionpayFindCarStatusRequest;
import com.icetech.paycenter.domain.autopay.response.FindCarStatusResponse;
import com.icetech.paycenter.domain.autopay.response.UnionpayFindCarStatusResponse;
import com.icetech.paycenter.mapper.autopay.ParkUnionpayDao;
import com.icetech.paycenter.service.BaseUnionpayService;
import com.icetech.paycenter.service.autopay.UnionpayNotifyService;
import com.icetech.paycenter.service.impl.FindCarStatusServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 银联无感支付-场内状态查询
 * @author fangct
 */
@Service
public class AutopayFindCarStatusServiceImpl implements UnionpayNotifyService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String TRANS_ID = "40802";

    @Autowired
    private ParkUnionpayDao parkUnionpayDao;
    @Autowired
    private FindCarStatusServiceImpl findCarStatusService;

    @Override
    public String dealNotify(UnionpayBaseNotifyRequest baseRequest) throws Exception {

        UnionpayFindCarStatusResponse unionpayFindCarStatusResponse = new UnionpayFindCarStatusResponse();
        unionpayFindCarStatusResponse.setTransId(TRANS_ID);
        unionpayFindCarStatusResponse.setRemark(baseRequest.getRemark());
        unionpayFindCarStatusResponse.setStatus(-1);

        UnionpayFindCarStatusRequest unionpayFindCarStatusRequest = (UnionpayFindCarStatusRequest) baseRequest;
        //验证参数
        if (Validator.validate(unionpayFindCarStatusRequest)) {

            String plateNum = unionpayFindCarStatusRequest.getCarNo();
            String outParkcode = unionpayFindCarStatusRequest.getParkCode();
            String orderNum = unionpayFindCarStatusRequest.getRefNo();

            ParkUnionpay parkUnionpay = new ParkUnionpay();
            parkUnionpay.setOutParkcode(outParkcode);
            parkUnionpay = parkUnionpayDao.selectById(parkUnionpay);
            AssertTools.notNull(parkUnionpay, CodeConstants.ERROR_402, "未查询到车场编号:" + outParkcode + "的信息");
            String parkCode = parkUnionpay.getParkCode();

            FindCarStatusRequest findCarStatusRequest = new FindCarStatusRequest();
            findCarStatusRequest.setParkCode(parkCode);
            findCarStatusRequest.setPlateNum(plateNum);
            findCarStatusRequest.setOrderNum(orderNum);

            logger.info("<场内状态查询接口> 准备请求车场，订单号：{}", orderNum);
            ObjectResponse<FindCarStatusResponse> objectResponse = findCarStatusService.findCarStatus(findCarStatusRequest);
            unionpayFindCarStatusResponse.setResultCode(BaseUnionpayService.ErrCode.成功.getCode());
            if (objectResponse != null) {

                if (CodeConstants.SUCCESS.equals(objectResponse.getCode())) {

                    FindCarStatusResponse findCarStatusResponse = objectResponse.getData();
                    Integer status = findCarStatusResponse.getStatus();

                    unionpayFindCarStatusResponse.setStatus(status);

                } else {
                    unionpayFindCarStatusResponse.setResultMsg(objectResponse.getMsg());
                }
            } else {
                unionpayFindCarStatusResponse.setResultMsg(CodeConstants.getName(CodeConstants.ERROR));
            }

        }else{
            unionpayFindCarStatusResponse.setResultCode(BaseUnionpayService.ErrCode.报文格式错误.getCode());
            unionpayFindCarStatusResponse.setResultMsg(BaseUnionpayService.ErrCode.报文格式错误.toString());
        }
        return DataChangeTools.bean2gson(unionpayFindCarStatusResponse);
    }
}
