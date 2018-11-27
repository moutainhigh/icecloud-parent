package com.icetech.cloudcenter.rpc;

import com.icetech.api.cloudcenter.model.request.ComplateOrderRequest;
import com.icetech.api.cloudcenter.service.ComplateOrderFeignApi;
import com.icetech.cloudcenter.dao.order.OrderEnexRecordDao;
import com.icetech.cloudcenter.dao.order.OrderInfoDao;
import com.icetech.cloudcenter.service.ExitPayDealServiceImpl;
import com.icetech.common.CodeTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.constants.EnexRecordTypeConstants;
import com.icetech.common.constants.OrderStatusConstants;
import com.icetech.common.domain.OrderEnexRecord;
import com.icetech.common.domain.OrderInfo;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.util.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ComplateOrderFeignClient implements ComplateOrderFeignApi {
    // 日志管理
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OrderInfoDao orderInfoDao;
    @Autowired
    private OrderEnexRecordDao orderEnexRecordDao;
    @Autowired
    private ExitPayDealServiceImpl exitPayDealServiceImpl;

    @Override
    public ObjectResponse complateOrder(ComplateOrderRequest complateOrderRequest) {
        Long parkId = complateOrderRequest.getParkId();
        String orderNum = CodeTools.GenerateOrderNum();

        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(complateOrderRequest, orderInfo);
        orderInfo.setOrderNum(orderNum);
        orderInfo.setServiceStatus(OrderStatusConstants.LEAVED_PARK);
        orderInfo.setOperAccount(complateOrderRequest.getUserAccount());
        orderInfo.setTotalPrice(complateOrderRequest.getTotalAmount());
        orderInfo.setDiscountPrice(complateOrderRequest.getDiscountAmount());
        orderInfo.setPaidPrice(complateOrderRequest.getPaidAmount());
        orderInfoDao.insert(orderInfo);
        logger.info("<完整订单记录上报服务> 插入订单信息表完成，orderNum：{}", orderNum);

        OrderEnexRecord orderEnexRecord_enter = new OrderEnexRecord();
        BeanUtils.copyProperties(complateOrderRequest, orderEnexRecord_enter);
        orderEnexRecord_enter.setRecordType(EnexRecordTypeConstants.ENTER);
        orderEnexRecord_enter.setOrderNum(orderNum);
        orderEnexRecord_enter.setEnterNo(complateOrderRequest.getEntraceName());
        orderEnexRecord_enter.setImage(complateOrderRequest.getEnterImage());
        orderEnexRecord_enter.setChannelId(complateOrderRequest.getInChannelId());
        orderEnexRecordDao.insert(orderEnexRecord_enter);
        logger.info("<离场服务> 插入订单出入表入场部分完成，orderNum：{}", orderNum);

        OrderEnexRecord orderEnexRecord_exit = orderEnexRecord_enter;
        orderEnexRecord_exit.setRecordType(EnexRecordTypeConstants.EXIT);
        orderEnexRecord_exit.setExitNo(complateOrderRequest.getExitName());
        orderEnexRecord_exit.setImage(complateOrderRequest.getExitImage());
        orderEnexRecord_exit.setChannelId(complateOrderRequest.getOutChannelId());
        orderEnexRecord_exit.setOperAccount (complateOrderRequest.getUserAccount());
        orderEnexRecordDao.insert(orderEnexRecord_exit);
        logger.info("<离场服务> 插入订单出入表离场部分完成，orderNum：{}", orderNum);

        ObjectResponse objectResponse = exitPayDealServiceImpl.exitPayDeal(complateOrderRequest.getPaidInfo(), parkId, orderNum);
        if (objectResponse.getCode().equals(CodeConstants.SUCCESS)){
            Map map = new HashMap();
            map.put("orderNum", orderNum);
            return ResponseUtils.returnSuccessResponse(map);
        }else{
            return objectResponse;
        }
    }
}
