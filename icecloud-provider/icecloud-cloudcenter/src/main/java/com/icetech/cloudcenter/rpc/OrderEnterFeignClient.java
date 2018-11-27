package com.icetech.cloudcenter.rpc;

import com.icetech.api.cloudcenter.model.request.EnterRequest;
import com.icetech.api.cloudcenter.service.OrderEnterFeignApi;
import com.icetech.api.paycenter.model.request.EnterNotifyRequestDto;
import com.icetech.api.paycenter.service.PayCenterFeignApi;
import com.icetech.cloudcenter.dao.order.OrderEnexRecordDao;
import com.icetech.cloudcenter.dao.order.OrderInfoDao;
import com.icetech.cloudcenter.dao.park.ParkConfigDao;
import com.icetech.cloudcenter.domain.order.update.OrderInfoUpdate;
import com.icetech.cloudcenter.domain.park.ParkConfig;
import com.icetech.common.CodeTools;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
public class OrderEnterFeignClient implements OrderEnterFeignApi {
    // 日志管理
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OrderInfoDao orderInfoDao;
    @Autowired
    private OrderEnexRecordDao orderEnexRecordDao;
    @Autowired
    private PayCenterFeignApi payCenterService;
    @Autowired
    private ParkConfigDao parkConfigDao;

    private void autoPayEnterNotify(EnterRequest enterRequest, Long parkId, String orderNum, String parkCode) {
        ParkConfig parkConfig = parkConfigDao.selectByParkId(parkId);
        if (Objects.nonNull(parkConfig)&&parkConfig.getIsNosenpayment()==1&&enterRequest.getType()==1){
            EnterNotifyRequestDto enterNotifyRequest = new EnterNotifyRequestDto();
            enterNotifyRequest.setParkCode(parkCode);
            enterNotifyRequest.setPlateNum(enterRequest.getPlateNum());
            enterNotifyRequest.setOrderNum(orderNum);
            enterNotifyRequest.setEnterTime(DateTools.secondTostring(Math.toIntExact(enterRequest.getEnterTime())));
            ObjectResponse<String> response = payCenterService.autoPayEnterNotify(enterNotifyRequest);
            if (!response.getCode().equals(CodeConstants.SUCCESS)){
                logger.info("<调用支付中心> 进场通知失败，orderNum：{}", orderNum);
            }
        }
    }

    /**
     * 判断是否有场内重复车牌，并修改状态为取消
     * @param plateNum
     * @return
     */
    private void checkSamePlate(String plateNum) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setPlateNum(plateNum);
        orderInfo.setServiceStatus(OrderStatusConstants.IN_PARK);
        OrderInfo orderInfo_res = orderInfoDao.selectById(orderInfo);
        if (orderInfo_res != null) {
            logger.info("<车辆入场服务> 重复入场，车牌号：{}", plateNum);
            OrderInfoUpdate orderInfoUpdate = new OrderInfoUpdate();
            OrderInfo orderInfoNeo = new OrderInfo();
            orderInfoNeo.setServiceStatus(OrderStatusConstants.CANCEL);
            orderInfoUpdate.setNeo(orderInfoNeo);

            OrderInfo orderInfoOld = new OrderInfo();
            orderInfoOld.setServiceStatus(OrderStatusConstants.IN_PARK);
            orderInfoOld.setPlateNum(plateNum);
            orderInfoUpdate.setOld(orderInfoOld);
            orderInfoDao.updateStatus(orderInfoUpdate);
        }
    }

    @Override
    public ObjectResponse<Map<String, Object>> enter(EnterRequest enterRequest, String parkCode) {
        String plateNum = enterRequest.getPlateNum();
        Long parkId = enterRequest.getParkId();
        //判断是否重复入场，并取消旧的订单记录
        checkSamePlate(plateNum);

        // 新增订单记录表
        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(enterRequest, orderInfo);
        String orderNum = CodeTools.GenerateOrderNum();
        orderInfo.setOrderNum(orderNum);
        orderInfoDao.insert(orderInfo);
        logger.info("<车辆入场服务> 插入订单信息表完成，orderNum：{}", orderNum);

        // 新增订单进出场记录表
        OrderEnexRecord orderEnexRecord = new OrderEnexRecord();
        BeanUtils.copyProperties(enterRequest, orderEnexRecord);
        orderEnexRecord.setRecordType(EnexRecordTypeConstants.ENTER);
        orderEnexRecord.setEnterNo(enterRequest.getEntranceName());

        String date = DateTools.getFormat(DateTools.DF_, new Date());
        String[] ymd = date.split("-");
        String imgFileName = parkCode + "/image/" + ymd[0] + ymd[1] + "/" + ymd[2] + "/"
                + enterRequest.getEnterImage();
        orderEnexRecord.setImage(imgFileName);
        orderEnexRecord.setOrderNum(orderNum);
        orderEnexRecordDao.insert(orderEnexRecord);
        logger.info("<车辆入场服务> 插入订单进出场表完成，orderNum：{}", orderNum);

        //调用支付中心进场通知(只有临时车和开启无感支付才需要调用接口)
        autoPayEnterNotify(enterRequest, parkId, orderNum, parkCode);

        //返回
        Map map = new HashMap();
        map.put("orderNum", orderNum);
        return ResponseUtils.returnSuccessResponse(map);
    }
}
