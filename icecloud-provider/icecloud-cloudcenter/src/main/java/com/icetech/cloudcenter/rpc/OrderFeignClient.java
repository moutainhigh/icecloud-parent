package com.icetech.cloudcenter.rpc;

import com.icetech.api.cloudcenter.model.request.QueryOrderFeeRequest;
import com.icetech.api.cloudcenter.model.request.SearchCarRequest;
import com.icetech.api.cloudcenter.model.response.QueryOrderFeeResponse;
import com.icetech.api.cloudcenter.model.response.SearchCarResponse;
import com.icetech.api.cloudcenter.service.OrderFeignApi;
import com.icetech.api.cloudcenter.service.QueryOrderFeeFeginApi;
import com.icetech.api.transcenter.service.OssFeignApi;
import com.icetech.cloudcenter.dao.order.OrderEnexRecordDao;
import com.icetech.cloudcenter.dao.order.OrderInfoDao;
import com.icetech.cloudcenter.dao.park.ParkDao;
import com.icetech.common.DateTools;
import com.icetech.common.ToolsUtil;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.constants.OrderStatusConstants;
import com.icetech.common.domain.OrderEnexRecord;
import com.icetech.common.domain.OrderInfo;
import com.icetech.common.domain.Park;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.domain.response.PageQuery;
import com.icetech.common.util.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("orderService")
public class OrderFeignClient implements OrderFeignApi {
    // 日志管理
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OrderInfoDao orderInfoDao;
    @Autowired
    private ParkDao parkDao;
    @Autowired
    private QueryOrderFeeFeginApi queryOrderFeeService;
    @Autowired
    private OrderEnexRecordDao orderEnexRecordDao;
    @Autowired
    private OssFeignApi ossService;
    /**
     * 月卡
     */
    private static final Integer MONTH_CARD = 2;
    /**
     * 无需支付
     */
    private static final Integer NO_NEED_PAY = 1;

    @Override
    public ObjectResponse<OrderInfo> findByOrderNum(String orderNum) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderNum(orderNum);
        orderInfo = orderInfoDao.selectById(orderInfo);
        if (orderInfo != null){
            return ResponseUtils.returnSuccessResponse(orderInfo);
        }else{
            logger.info("根据订单号查询订单，未找到结果，参数：{}", orderNum);
            return ResponseUtils.returnErrorResponse(CodeConstants.ERROR_404);
        }
    }

    @Override
    public ObjectResponse<OrderInfo> findInPark(String plateNum, String parkCode) {
        if (ToolsUtil.isNull(plateNum)){
            return ResponseUtils.returnErrorResponse(CodeConstants.ERROR_400);
        }
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setPlateNum(plateNum);
        Park park = parkDao.selectByCode(parkCode);
        if (park == null)
            return ResponseUtils.returnErrorResponse(CodeConstants.ERROR_402, "车场未注册");
        orderInfo.setParkId(park.getId());
        orderInfo = orderInfoDao.selectById(orderInfo);
        if (orderInfo != null && orderInfo.getServiceStatus().equals(OrderStatusConstants.IN_PARK)){
            return ResponseUtils.returnSuccessResponse(orderInfo);
        }else{
            logger.info("根据车场编号和车牌号未查询到场内订单，参数：{}，{}", plateNum, parkCode);
            return ResponseUtils.returnErrorResponse(CodeConstants.ERROR_404);
        }
    }

    @Override
    public ObjectResponse<List<SearchCarResponse>> searchCarInfo(PageQuery<SearchCarRequest> pageQuery) {
        SearchCarRequest searchCarRequest = pageQuery.getParam();
        String parkCode = searchCarRequest.getParkCode();
        String plateNum = searchCarRequest.getPlateNum();
        Park park = parkDao.selectByCode(parkCode);
        if (park == null)
            return ResponseUtils.returnErrorResponse(CodeConstants.ERROR_402, "车场未注册");
        Long parkId = park.getId();

        List<OrderInfo> orderInfos = getOrderInfos(pageQuery, plateNum, parkId);
        if (orderInfos != null && orderInfos.size() > 0){
            List<SearchCarResponse> list = new ArrayList<SearchCarResponse>();
            for (OrderInfo orderInfo1 : orderInfos){
                SearchCarResponse searchCarResponse = getSearchCarResponse(parkCode, plateNum, orderInfo1);
                list.add(searchCarResponse);
            }
            return ResponseUtils.returnSuccessResponse(list);
        }else{
            return ResponseUtils.returnErrorResponse(CodeConstants.ERROR_404);
        }
    }

    /**
     * 获取响应结果
     * @param parkCode
     * @param plateNum
     * @param orderInfo1
     * @return
     */
    private SearchCarResponse getSearchCarResponse(String parkCode, String plateNum, OrderInfo orderInfo1) {
        SearchCarResponse searchCarResponse = new SearchCarResponse();
        searchCarResponse.setOrderNum(orderInfo1.getOrderNum());
        searchCarResponse.setCarType(orderInfo1.getType());
        searchCarResponse.setEnterTime(DateTools.secondTostring(orderInfo1.getEnterTime().intValue()));
        searchCarResponse.setPlateNum(orderInfo1.getPlateNum());

        if (MONTH_CARD ==orderInfo1.getType()){
            searchCarResponse.setPayStatus(NO_NEED_PAY);
        }else {
            QueryOrderFeeRequest queryOrderFeeRequest = new QueryOrderFeeRequest();
            queryOrderFeeRequest.setParkCode(parkCode);
            queryOrderFeeRequest.setPlateNum(plateNum);
            ObjectResponse<QueryOrderFeeResponse> objectResponse = queryOrderFeeService.queryOrderFee(queryOrderFeeRequest);
            QueryOrderFeeResponse queryOrderFeeResponse = objectResponse.getData();
            Integer status = queryOrderFeeResponse.getStatus();
            searchCarResponse.setPayStatus(status);
        }
        OrderEnexRecord orderEnexRecord = new OrderEnexRecord();
        orderEnexRecord.setOrderNum(orderInfo1.getOrderNum());
        orderEnexRecord.setRecordType(1);
        orderEnexRecord = orderEnexRecordDao.selectById(orderEnexRecord);
        String enterImage = orderEnexRecord.getImage();
        if (ToolsUtil.isNotNull(enterImage)){
            //TODO
//            searchCarResponse.setEnterImage(ossService.getImageUrl(enterImage));
            searchCarResponse.setEnterImage(enterImage);
        }
        return searchCarResponse;
    }

    /**
     * 根据车牌查询订单集合X
     * @param pageQuery
     * @param plateNum
     * @param parkId
     * @return
     */
    private List<OrderInfo> getOrderInfos(PageQuery<SearchCarRequest> pageQuery, String plateNum, Long parkId) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setPlateNum(plateNum);
        orderInfo.setParkId(parkId);
        PageQuery<OrderInfo> pageQuery1 = new PageQuery<>();
        BeanUtils.copyProperties(pageQuery, pageQuery1);
        pageQuery1.setParam(orderInfo);
        return orderInfoDao.selectList(pageQuery1);
    }
}
