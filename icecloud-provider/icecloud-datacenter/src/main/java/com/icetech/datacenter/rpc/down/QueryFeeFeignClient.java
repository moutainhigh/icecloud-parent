package com.icetech.datacenter.rpc.down;

import com.icetech.api.datacenter.model.request.QueryFeeRequest;
import com.icetech.api.datacenter.model.response.QueryFeeResponse;
import com.icetech.api.datacenter.service.QueryFeeFeignApi;
import com.icetech.common.AssertTools;
import com.icetech.common.DataChangeTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.util.ResponseUtils;
import com.icetech.datacenter.common.constant.DCTimeOutConstants;
import com.icetech.datacenter.common.enumeration.DownServiceEnum;
import com.icetech.datacenter.service.handle.DownHandle;
import com.icetech.datacenter.service.handle.PublicHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 缴费查询接口
 *
 * @author fangct
 */
@Service
public class QueryFeeFeignClient implements QueryFeeFeignApi {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DownHandle downHandle;
    @Autowired
    private PublicHandle publicHandle;

    @Override
    public ObjectResponse queryFee(QueryFeeRequest queryFeeRequest) {

        String key = queryFeeRequest.getKey();
        Long parkId = queryFeeRequest.getParkId();

        //删除请求对象中的多余参数
        queryFeeRequest.setKey(null);
        queryFeeRequest.setParkId(null);
        String messageId =
                downHandle.signAndSend(parkId, queryFeeRequest.getParkCode(),
                        key, DownServiceEnum.缴费查询.getServiceName(),queryFeeRequest, null);
        AssertTools.notNull(messageId, CodeConstants.ERROR_3001, "下发消息失败");
        String data = publicHandle.getDataFromRedis(messageId, DCTimeOutConstants.QUERYFEE_TIMEOUT);
        AssertTools.notNull(data, CodeConstants.ERROR_3001, "缴费查询超时或失败");
        QueryFeeResponse queryFeeResponse = DataChangeTools.gson2bean(data, QueryFeeResponse.class);
        if (queryFeeResponse.getPlateNum() == null){
            queryFeeResponse.setPlateNum(queryFeeRequest.getPlateNum());
        }
        return ResponseUtils.returnSuccessResponse(queryFeeResponse);
    }

}
