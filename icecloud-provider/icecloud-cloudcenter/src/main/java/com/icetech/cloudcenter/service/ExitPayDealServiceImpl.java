package com.icetech.cloudcenter.service;

import com.icetech.api.cloudcenter.model.request.ExitCommonRequest;
import com.icetech.api.cloudcenter.model.request.ExitRequest;
import com.icetech.cloudcenter.dao.order.OrderDiscountDao;
import com.icetech.cloudcenter.dao.order.OrderPayDao;
import com.icetech.cloudcenter.domain.order.query.OrderDiscountQuery;
import com.icetech.cloudcenter.domain.order.query.OrderPayQuery;
import com.icetech.common.DateTools;
import com.icetech.common.ToolsUtil;
import com.icetech.common.constants.PayStatusConstants;
import com.icetech.common.domain.OrderDiscount;
import com.icetech.common.domain.OrderPay;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.util.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Description : 离场时支付记录的处理业务
 * @author fangct
 */
@Service
public class ExitPayDealServiceImpl {
    // 日志管理
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private OrderPayDao orderPayDao;
    @Autowired
    private OrderDiscountDao orderDiscountDao;

    public ObjectResponse exitPayDeal(List<ExitCommonRequest.PaidInfo> paidInfos, Long parkId, String orderNum){
        int paySize = paidInfos.size();
        // 添加子订单
        if (paySize > 0) {
            for (int i = 0; i < paidInfos.size(); i++) {
                ExitRequest.PaidInfo paidInfo = paidInfos.get(i);
                String tradeNo = paidInfo.getTradeNo();

                OrderPay orderPay = new OrderPay();
                orderPay.setTradeNo(tradeNo);
                orderPay.setParkId(parkId);
                OrderPay orderPay_result = orderPayDao.selectById(orderPay);
                if (orderPay_result == null) {
                    OrderPay orderPay_para = new OrderPay();
                    orderPay_para.setParkId(parkId);
                    orderPay_para.setOrderNum(orderNum);
                    orderPay_para.setTradeNo(tradeNo);
                    orderPay_para.setPayStatus(PayStatusConstants.PAID);
                    orderPay_para.setPayTime(paidInfo.getPayTime());
                    orderPay_para.setTotalPrice(paidInfo.getTotalPrice());
                    orderPay_para.setPaidPrice(paidInfo.getPaidPrice());
                    orderPay_para.setDiscountPrice(paidInfo.getDiscountPrice());
                    orderPay_para.setPayChannel(paidInfo.getPayChannel());
                    orderPay_para.setPayWay(paidInfo.getPayWay());
                    orderPay_para.setPayTerminal(paidInfo.getPayTerminal());
                    //lastPayTime参数的设置

                    orderPayDao.insert(orderPay_para);
                    logger.info("<离场服务> 插入订单交易记录完成，orderNum：{}",orderNum);
                }
                double discountPrice = ToolsUtil.parseDouble(paidInfo.getDiscountPrice());
                if (discountPrice > 0) {
                    List<ExitRequest.DiscountInfo> discountInfos = paidInfo.getDiscountInfo();
                    int discountSize = discountInfos.size();
                    for (int j = 0; j < discountSize; j++) {
                        ExitRequest.DiscountInfo discountInfo = discountInfos.get(j);
                        OrderDiscount orderDiscount_para = new OrderDiscount();
                        orderDiscount_para.setParkId(parkId);
                        orderDiscount_para.setDiscountNo(discountInfo.getDiscountNo());

                        OrderDiscount discount_result = orderDiscountDao.selectById(orderDiscount_para);
                        if (discount_result == null) {
                            // 添加优惠记录
                            OrderDiscount orderDiscount = new OrderDiscount();
                            orderDiscount.setParkId(parkId);
                            orderDiscount.setOrderNum(orderNum);
                            orderDiscount.setTradeNo(tradeNo);
                            orderDiscount.setFrom(0);//本地优惠
                            orderDiscount.setDiscountNo(discountInfo.getDiscountNo());
                            orderDiscount.setType(discountInfo.getDiscountType());
                            orderDiscount.setAmount(discountInfo.getDiscountNumber());
                            orderDiscount.setStatus(1);
                            orderDiscount.setSendTime(
                                    DateTools.secondTostring(discountInfo.getDiscountTime().intValue()));
                            orderDiscount.setUseTime(DateTools.getFormat(new Date()));
                            orderDiscountDao.insert(orderDiscount);
                            logger.info("<离场服务> 插入订单优惠记录完成，orderNum：{}",orderNum);
                        }
                    }
                }
            }
        }

        // 修改无效交易记录状态
        OrderPayQuery orderPayQuery = new OrderPayQuery();
        orderPayQuery.setOrderNum(orderNum);
        orderPayQuery.setOldPayStatus(PayStatusConstants.PAYING);
        orderPayQuery.setOldPayStatus(PayStatusConstants.CANCEL);
        orderPayDao.updateStatus(orderPayQuery);
        logger.info("<离场服务> 修改无效交易记录状态，orderNum：{}",orderNum);

        // 更新优惠记录
        OrderDiscountQuery orderDiscountQuery = new OrderDiscountQuery();
        orderDiscountQuery.setParkId(parkId);
        orderDiscountQuery.setOrderNum(orderNum);
        orderDiscountQuery.setNewStatus(2);
        orderDiscountQuery.setOldStatus(0);
        orderDiscountDao.updateStatus(orderDiscountQuery);
        logger.info("<离场服务> 修改无效交易记录状态，orderNum：{}",orderNum);
        return ResponseUtils.returnSuccessResponse();
    }
}
