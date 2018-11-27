package com.icetech.datacenter.rpc.send;

import com.icetech.api.datacenter.model.request.SendRequest;
import com.icetech.api.datacenter.service.SendFeignApi;
import com.icetech.common.AssertTools;
import com.icetech.common.DateTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.util.ResponseUtils;
import com.icetech.datacenter.common.enumeration.DownServiceEnum;
import com.icetech.datacenter.dao.MonthProductDao;
import com.icetech.datacenter.dao.MonthRecordDao;
import com.icetech.datacenter.domain.MonthProduct;
import com.icetech.datacenter.domain.MonthRecord;
import com.icetech.datacenter.domain.request.IssuedCardRequest;
import com.icetech.datacenter.service.handle.DownHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 月卡信息下发接口
 *
 * @author fangct
 */
@Service
public class IssuedCardFeignClient implements SendFeignApi {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DownHandle downHandle;
    @Autowired
    private MonthRecordDao monthRecordDao;
    @Autowired
    private MonthProductDao monthProductDao;

    /**
     * 全天卡
     */
    private static final int CARD_TYPE_0 = 0;
    /**
     * 分时段卡
     */
    private static final int CARD_TYPE_1 = 1;

    @Override
    public ObjectResponse send(SendRequest sendRequest) {
        Long serviceId = sendRequest.getServiceId();

        MonthRecord monthRecord = monthRecordDao.selectById(serviceId);

        if (monthRecord == null){
            return ResponseUtils.returnErrorResponse(CodeConstants.ERROR_404);
        }
        IssuedCardRequest issuedCardRequest = buildRequest(monthRecord);
        String messageId = downHandle.signAndSend(sendRequest.getParkId(), DownServiceEnum.月卡.getServiceName(),
                issuedCardRequest, serviceId);
        AssertTools.notNull(messageId, CodeConstants.ERROR, "下发消息失败");
        return ResponseUtils.returnSuccessResponse();
    }

    /**
     * 构建请求参数
     *
     * @param monthRecord
     * @return
     */
    private IssuedCardRequest buildRequest(MonthRecord monthRecord) {
        IssuedCardRequest issuedCardRequest = new IssuedCardRequest();
        issuedCardRequest.setCardId(String.valueOf(monthRecord.getMonthId()));
        if (monthRecord.getPlateNum() != null){
            issuedCardRequest.setPlateNum(monthRecord.getPlateNum().replaceAll(",", "/"));
        }
        issuedCardRequest.setPhone(monthRecord.getPhone());
        issuedCardRequest.setCardOwner(monthRecord.getCardOwner());
        issuedCardRequest.setCardProperty(monthRecord.getCardProperty());
        issuedCardRequest.setStartDate(monthRecord.getStartTime());
        issuedCardRequest.setEndDate(monthRecord.getEndTime());
        issuedCardRequest.setCardOperType(monthRecord.getCardOpertype());
        issuedCardRequest.setPlotCount(monthRecord.getPlotCount());
        issuedCardRequest.setPlotNum(monthRecord.getSpaceNum());
        //Todo 退款金额
        issuedCardRequest.setPayMoney(monthRecord.getPayMoney());
        issuedCardRequest.setOperAccount(monthRecord.getOperAccount());
        issuedCardRequest.setOperTime(DateTools.timeStr2seconds(monthRecord.getCreateTime()));

        MonthProduct monthProduct = monthProductDao.selectById(monthRecord.getProductId());
        issuedCardRequest.setProdName(monthProduct.getName());
        issuedCardRequest.setProdDuration(monthProduct.getDuration());
        int cardType = monthProduct.getCardType();
        issuedCardRequest.setCardType(cardType);
        if (cardType == CARD_TYPE_1){
                issuedCardRequest.setStartTime(monthRecord.getStartTime());
                issuedCardRequest.setEndTime(monthRecord.getEndTime());
        }
        return issuedCardRequest;
    }
}
