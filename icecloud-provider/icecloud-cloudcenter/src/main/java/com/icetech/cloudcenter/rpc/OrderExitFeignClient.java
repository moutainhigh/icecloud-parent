package com.icetech.cloudcenter.rpc;

import com.icetech.api.cloudcenter.model.request.ExitCommonRequest;
import com.icetech.api.cloudcenter.model.request.ExitRequest;
import com.icetech.api.cloudcenter.service.OrderExitFeignApi;
import com.icetech.api.paycenter.model.request.ExitNotifyRequestDto;
import com.icetech.api.paycenter.service.PayCenterFeignApi;
import com.icetech.cloudcenter.dao.order.OrderEnexRecordDao;
import com.icetech.cloudcenter.dao.order.OrderInfoDao;
import com.icetech.cloudcenter.dao.park.ParkConfigDao;
import com.icetech.cloudcenter.domain.park.ParkConfig;
import com.icetech.cloudcenter.service.ExitPayDealServiceImpl;
import com.icetech.common.AssertTools;
import com.icetech.common.DateTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.constants.EnexRecordTypeConstants;
import com.icetech.common.constants.OrderStatusConstants;
import com.icetech.common.domain.OrderEnexRecord;
import com.icetech.common.domain.OrderInfo;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.util.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service("orderExitService")
public class OrderExitFeignClient implements OrderExitFeignApi {
    // 日志管理
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OrderInfoDao orderInfoDao;
    @Autowired
    private OrderEnexRecordDao orderEnexRecordDao;
    @Autowired
    private ExitPayDealServiceImpl exitPayDealService;
    @Autowired
    private PayCenterFeignApi payCenterService;
    @Autowired
    private ParkConfigDao parkConfigDao;

    private void autoPayExitNotify(ExitRequest exitRequest, Long parkId, String orderNum, OrderEnexRecord orderEnexRecord_result, String parkCode) {
        ParkConfig parkConfig = parkConfigDao.selectByParkId(parkId);
        if (Objects.nonNull(parkConfig)&&parkConfig.getIsNosenpayment()==1&&orderEnexRecord_result.getType()==1){
            ExitNotifyRequestDto exitNotifyRequest = new ExitNotifyRequestDto();
            exitNotifyRequest.setParkCode(parkCode);
            exitNotifyRequest.setPlateNum(exitRequest.getPlateNum());
            exitNotifyRequest.setOrderNum(orderNum);
            exitNotifyRequest.setExitTime(DateTools.secondTostring(Math.toIntExact(exitRequest.getExitTime())));
            ObjectResponse<String> response = payCenterService.autoPayExitNotify(exitNotifyRequest);
            if (!response.getCode().equals(CodeConstants.SUCCESS)){
                logger.info("<调用支付中心> 离场通知失败，orderNum：{}", orderNum);
            }
        }
    }

    @Override
    public ObjectResponse exit(ExitRequest exitRequest,String parkCode) {
        Long parkId = exitRequest.getParkId();
        String orderNum = exitRequest.getOrderNum();

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderNum(orderNum);
        orderInfo.setParkId(parkId);
        OrderInfo orderInfoResult = orderInfoDao.selectById(orderInfo);
        if (orderInfoResult == null){
            logger.info("<离场服务> 未查询到订单，参数：[{},{}]",parkId, orderNum);
            return new ObjectResponse(CodeConstants.ERROR_404,
                    CodeConstants.getName(CodeConstants.ERROR_404));
        }

        int serviceStatus = orderInfoResult.getServiceStatus();
        if (serviceStatus == OrderStatusConstants.LEAVED_PARK){
            logger.info("<离场服务> 重复离场，重复请求参数：{}",exitRequest);
            return new ObjectResponse(CodeConstants.ERROR_405,
                    CodeConstants.getName(CodeConstants.ERROR_405));
        } else {
            //  修改订单状态为离场
            OrderInfo orderInfo_update_para = new OrderInfo();
            orderInfo_update_para.setOrderNum(orderNum);
            orderInfo_update_para.setParkId(parkId);
            orderInfo_update_para.setServiceStatus(OrderStatusConstants.LEAVED_PARK);
            orderInfo_update_para.setExitTime(exitRequest.getExitTime());
            orderInfo_update_para.setTotalPrice(exitRequest.getTotalAmount());
            orderInfo_update_para.setPaidPrice(exitRequest.getPaidAmount());
            orderInfo_update_para.setDiscountPrice(exitRequest.getDiscountAmount());
            orderInfo_update_para.setOperAccount(exitRequest.getUserAccount());
            orderInfoDao.update(orderInfo_update_para);
            logger.info("<离场服务> 修改订单主信息完成，orderNum：{}", orderNum);

            OrderEnexRecord orderEnexRecord = new OrderEnexRecord();
            orderEnexRecord.setRecordType(EnexRecordTypeConstants.ENTER);
            orderEnexRecord.setOrderNum(orderNum);
            OrderEnexRecord orderEnexRecord_result = orderEnexRecordDao.selectById(orderEnexRecord);

            AssertTools.notNull(orderEnexRecord_result, CodeConstants.ERROR, "平台订单数据不完整");
            orderEnexRecord_result.setCreateTime(null);
            orderEnexRecord_result.setId(null);
            orderEnexRecord_result.setRecordType(EnexRecordTypeConstants.EXIT);

            String date = DateTools.getFormat(DateTools.DF_, new Date());
            String[] ymd = date.split("-");
            String imgFileName = parkCode + "/image/" + ymd[0] + ymd[1] + "/" + ymd[2] + "/"
                    + exitRequest.getExitImage();
            orderEnexRecord.setImage(imgFileName);
            orderEnexRecord_result.setChannelId(exitRequest.getChannelId());
            orderEnexRecord_result.setExitNo(exitRequest.getExitName());
            orderEnexRecord_result.setExitTime(exitRequest.getExitTime());
            orderEnexRecord_result.setOperAccount(exitRequest.getUserAccount());

            orderEnexRecordDao.insert(orderEnexRecord_result);
            logger.info("<离场服务> 插入订单出入表完成，orderNum：{}", orderNum);
            //调用支付中心离场通知(只有临时车和开启无感支付才需要调用接口)
            autoPayExitNotify(exitRequest, parkId, orderNum, orderEnexRecord_result, parkCode);


            List<ExitCommonRequest.PaidInfo> paidInfo = exitRequest.getPaidInfo();
            if (paidInfo == null)
                return ResponseUtils.returnSuccessResponse();

            return exitPayDealService.exitPayDeal(paidInfo, parkId, orderNum);
        }
    }
}
