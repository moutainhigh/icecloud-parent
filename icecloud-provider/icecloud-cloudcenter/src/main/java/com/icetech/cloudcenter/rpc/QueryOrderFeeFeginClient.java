package com.icetech.cloudcenter.rpc;

import com.icetech.api.cloudcenter.model.request.QueryOrderFeeRequest;
import com.icetech.api.cloudcenter.model.response.QueryOrderFeeResponse;
import com.icetech.api.cloudcenter.service.QueryOrderFeeFeginApi;
import com.icetech.api.datacenter.model.request.QueryFeeRequest;
import com.icetech.api.datacenter.model.response.QueryFeeResponse;
import com.icetech.api.datacenter.service.QueryFeeFeignApi;
import com.icetech.cloudcenter.dao.order.OrderInfoDao;
import com.icetech.cloudcenter.dao.park.ParkDao;
import com.icetech.common.AssertTools;
import com.icetech.common.ToolsUtil;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.domain.OrderInfo;
import com.icetech.common.domain.Park;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.exception.ResponseBodyException;
import com.icetech.common.util.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("queryOrderFeeService")
public class QueryOrderFeeFeginClient implements QueryOrderFeeFeginApi {
    // 日志管理
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private QueryFeeFeignApi queryFeeService;
    @Autowired
    private ParkDao parkDao;
    @Autowired
    private OrderInfoDao orderInfoDao;

    @Override
    public ObjectResponse<QueryOrderFeeResponse> queryOrderFee(QueryOrderFeeRequest queryOrderFeeRequest) {
        String parkCode = queryOrderFeeRequest.getParkCode();
        String channelId = queryOrderFeeRequest.getChannelId();
        String plateNum = queryOrderFeeRequest.getPlateNum();
        String orderNum = queryOrderFeeRequest.getOrderNum();
        if (ToolsUtil.isNotNull(parkCode) && (ToolsUtil.isNotNull(channelId) || ToolsUtil.isNotNull(plateNum))
                || ToolsUtil.isNotNull(orderNum)) {
            try {
                return execute(queryOrderFeeRequest);
            } catch (ResponseBodyException e) {
                return ResponseUtils.returnErrorResponse(e.getErrCode(), e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseUtils.returnErrorResponse(CodeConstants.ERROR_3001);
            }
        } else {
            return ResponseUtils.returnErrorResponse(CodeConstants.ERROR_400);
        }
    }

    private ObjectResponse<QueryOrderFeeResponse> execute(QueryOrderFeeRequest queryOrderFeeRequest) {
        QueryOrderFeeResponse queryOrderFeeResponse = new QueryOrderFeeResponse();

        String orderNum = queryOrderFeeRequest.getOrderNum();
        String plateNum = queryOrderFeeRequest.getPlateNum();
        //有无车牌号参数的标识
        boolean plateNumFlag = plateNum != null ? true : false;
        OrderInfo orderInfo = new OrderInfo();
        Park park = new Park();
        //有订单号查询
        if (ToolsUtil.isNotNull(orderNum)){
            orderInfo.setOrderNum(orderNum);
            orderInfo = orderInfoDao.selectById(orderInfo);
            park = parkDao.selectById(orderInfo.getParkId());
            AssertTools.notNull(park, CodeConstants.ERROR_402, "车场未注册");
        }else{
            park = parkDao.selectByCode(queryOrderFeeRequest.getParkCode());
            AssertTools.notNull(park, CodeConstants.ERROR_402, "车场未注册");

            if (plateNumFlag){
                orderInfo.setPlateNum(queryOrderFeeRequest.getPlateNum());
                orderInfo.setParkId(park.getId());
                orderInfo = orderInfoDao.selectById(orderInfo);
            }

        }
        AssertTools.notNull(orderInfo, CodeConstants.ERROR_402, "未找到车牌在场记录");

        //查询DataCenter
        QueryFeeRequest queryFeeRequest = new QueryFeeRequest();
        queryFeeRequest.setPlateNum(orderInfo.getPlateNum());
        queryFeeRequest.setParkId(park.getId());
        queryFeeRequest.setOrderNum(orderInfo.getOrderNum());
        queryFeeRequest.setChannelId(queryOrderFeeRequest.getChannelId());
        queryFeeRequest.setParkCode(park.getParkCode());
        queryFeeRequest.setKey(park.getKey());
        ObjectResponse<QueryFeeResponse> objectResponse = queryFeeService.queryFee(queryFeeRequest);

        //返回错误结果时抛出异常
        ResponseUtils.notError(objectResponse);

        QueryFeeResponse queryFeeResponse = objectResponse.getData();
        BeanUtils.copyProperties(queryFeeResponse, queryOrderFeeResponse);

        //有牌车出口支付
        if (!plateNumFlag){
            OrderInfo orderInfo_par = new OrderInfo();
            orderInfo_par.setOrderNum(queryFeeResponse.getOrderNum());
            orderInfo_par.setParkId(park.getId());
            orderInfo = orderInfoDao.selectById(orderInfo_par);
            AssertTools.notNull(orderInfo, CodeConstants.ERROR_402, "未找到车牌在场记录");
        }

        Double paidPrice = ToolsUtil.parseDouble(queryOrderFeeResponse.getPaidAmount());
        Double unpayPrice = ToolsUtil.parseDouble(queryOrderFeeResponse.getUnpayPrice());
        Long pay_time = queryOrderFeeResponse.getPayTime();
        Integer status = null;
        if (pay_time != null && 0 == pay_time) {
            logger.info("<缴费查询> 本地返回结果纠正，删除pay_time，参数=[{}]， 查询结果=[{}]", queryFeeRequest, queryFeeResponse);
            queryOrderFeeResponse.setParkTime(null);
        }
        //无待缴费且无已缴费金额时
        if (unpayPrice <= 0 && paidPrice <= 0) {
            status = 1; // 免费时长内无需缴费
        } else if (unpayPrice > 0 && paidPrice <= 0) {
            status = 2; // 初次缴费
        } else if (unpayPrice <= 0 && paidPrice > 0) {
            status = 3; // 已缴费未超时
        } else if (unpayPrice > 0 && paidPrice > 0) {
            status = 4; // 已缴费已超时
        }
        queryOrderFeeResponse.setStatus(status);
        queryOrderFeeResponse.setEnterTime(orderInfo.getEnterTime());
        queryOrderFeeResponse.setParkName(park.getParkName());
        return ResponseUtils.returnSuccessResponse(queryOrderFeeResponse);
    }
}
