package com.icetech.datacenter.rpc.send;

import com.icetech.api.cloudcenter.service.OrderDiscountFeignApi;
import com.icetech.api.cloudcenter.service.OrderFeignApi;
import com.icetech.api.cloudcenter.service.OrderPayFeignApi;
import com.icetech.api.datacenter.model.request.SendRequest;
import com.icetech.api.datacenter.service.SendFeignApi;
import com.icetech.common.AssertTools;
import com.icetech.common.ToolsUtil;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.domain.OrderInfo;
import com.icetech.common.domain.OrderPay;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.exception.ResponseBodyException;
import com.icetech.common.util.ResponseUtils;
import com.icetech.datacenter.common.enumeration.DownServiceEnum;
import com.icetech.datacenter.domain.request.NotifyPrepayRequest;
import com.icetech.datacenter.service.handle.DownHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 缴费通知下发接口
 *
 * @author fangct
 */
@Service
public class NotifyPrepayFeignClient implements SendFeignApi {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OrderPayFeignApi orderPayService;
    @Autowired
    private OrderFeignApi orderService;
    @Autowired
    private DownHandle downHandle;
    @Autowired
    private OrderDiscountFeignApi orderDiscountService;

    @Override
    public ObjectResponse send(SendRequest sendRequest) {
        Long serviceId = sendRequest.getServiceId();

        OrderPay orderPay = new OrderPay();
        orderPay.setId(serviceId);
        ObjectResponse<OrderPay> objectResponse = orderPayService.findOne(orderPay);
        ResponseUtils.notError(objectResponse);
        OrderPay orderPay_res = objectResponse.getData();

        NotifyPrepayRequest notifyPrepayRequest = buildRequest(orderPay_res);
        String messageId = downHandle.signAndSend(orderPay_res.getParkId(), DownServiceEnum.预缴费.getServiceName(),
                notifyPrepayRequest, serviceId);
        AssertTools.notNull(messageId, CodeConstants.ERROR, "下发消息失败");
        return ResponseUtils.returnSuccessResponse();

    }

    /**
     * 构建请求参数
     *
     * @param orderPay
     * @return
     */
    private NotifyPrepayRequest buildRequest(OrderPay orderPay) {
        NotifyPrepayRequest notifyPrepayRequest = new NotifyPrepayRequest();
        notifyPrepayRequest.setOrderNum(orderPay.getOrderNum());
        ObjectResponse<OrderInfo> objectResponse = orderService.findByOrderNum(orderPay.getOrderNum());
        if (CodeConstants.SUCCESS.equals(objectResponse.getCode())) {
            OrderInfo orderInfo = objectResponse.getData();
            notifyPrepayRequest.setPlateNum(orderInfo.getPlateNum());
        } else {
            logger.info("Dubbo根据订单号查询未找到记录，订单号：{}，返回结果：{}", orderPay.getOrderNum(), objectResponse);
            throw new ResponseBodyException(objectResponse.getCode(), "订单号不存在");
        }
        if (ToolsUtil.isNotNull(orderPay.getChannelId())) {
            notifyPrepayRequest.setChannelId(orderPay.getChannelId());
        }
        notifyPrepayRequest.setTradeNo(orderPay.getTradeNo());
        notifyPrepayRequest.setTotalPrice(orderPay.getTotalPrice());
        notifyPrepayRequest.setPrepay(orderPay.getPaidPrice());
        notifyPrepayRequest.setDiscountPrice(orderPay.getDiscountPrice());
        ObjectResponse<Map<String, String>> objectResponse2 = orderDiscountService.findDiscountNos(orderPay.getTradeNo(), orderPay.getParkId());
        if (CodeConstants.SUCCESS.equals(objectResponse2.getCode())) {
            notifyPrepayRequest.setDiscountNos(objectResponse2.getData().get("discountNos"));
        }
        notifyPrepayRequest.setPayWay(orderPay.getPayWay());
        notifyPrepayRequest.setPayChannel(orderPay.getPayChannel());
        notifyPrepayRequest.setPayTerminal(orderPay.getPayTerminal());
        notifyPrepayRequest.setPayTime(orderPay.getPayTime());
        return notifyPrepayRequest;
    }
}
