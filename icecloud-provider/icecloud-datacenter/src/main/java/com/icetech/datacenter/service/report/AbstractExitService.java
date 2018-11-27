package com.icetech.datacenter.service.report;

import com.icetech.api.cloudcenter.model.request.ExitCommonRequest;
import com.icetech.api.cloudcenter.model.request.ExitRequest;
import com.icetech.common.ToolsUtil;
import com.icetech.common.validator.Validator;
import com.icetech.datacenter.service.AbstractService;

import java.util.List;

public class AbstractExitService extends AbstractService {

    /**
     * 验证参数
     *
     * @param instance
     * @return
     */
    protected boolean verifyParams(ExitCommonRequest instance) {
        boolean flag = true;
        super.verifyParams(instance);
        String totalAmount = instance.getTotalAmount();
        if (ToolsUtil.parseDouble(totalAmount) > 0){
            List<ExitRequest.PaidInfo> paidInfos = instance.getPaidInfo();
            if (paidInfos != null && paidInfos.size() > 0) {
                L1:
                for (ExitRequest.PaidInfo paidInfo : paidInfos) {
                    try {
                        if (!Validator.validate(paidInfo)) {
                            flag = false;
                            break;
                        } else {
                            String discountPrice = paidInfo.getDiscountPrice();
                            if (ToolsUtil.parseDouble(discountPrice) > 0){
                                List<ExitRequest.DiscountInfo> discountInfos = paidInfo.getDiscountInfo();
                                if (discountInfos != null && discountInfos.size() > 0) {
                                    L2:
                                    for (ExitRequest.DiscountInfo discountInfo : discountInfos) {
                                        try {
                                            if (!Validator.validate(discountInfo)) {
                                                flag = false;
                                                break L1;
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    logger.info("交易号：{}的优惠金额大于0，但没有优惠明细", paidInfo.getTradeNo());
                                    flag = false;
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        flag = false;
                    }
                }
            }else{
                logger.info("订单号：{}的应缴费金额大于0，但没有支付明细", instance.getOrderNum());
                flag = false;
            }
        }
        return flag;
    }

}
