package com.icetech.datacenter.service.report.impl;

import com.icetech.common.DataChangeTools;
import com.icetech.common.DateTools;
import com.icetech.common.ResultTools;
import com.icetech.common.ToolsUtil;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.constants.TimeOutConstants;
import com.icetech.datacenter.dao.HeartbeatDao;
import com.icetech.datacenter.dao.HeartbeatOfflineDao;
import com.icetech.datacenter.dao.ParkFreespaceDao;
import com.icetech.datacenter.domain.Heartbeat;
import com.icetech.datacenter.domain.HeartbeatOffline;
import com.icetech.datacenter.domain.ParkFreespace;
import com.icetech.datacenter.domain.request.DataCenterBaseRequest;
import com.icetech.datacenter.domain.request.ParkStatusRequest;
import com.icetech.datacenter.domain.response.ParkStatusResponse;
import com.icetech.datacenter.service.AbstractService;
import com.icetech.datacenter.service.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParkStatusServiceImpl extends AbstractService implements ReportService {

    @Autowired
    private HeartbeatDao heartbeatDao;
    @Autowired
    private HeartbeatOfflineDao heartbeatOfflineDao;
    @Autowired
    private ParkFreespaceDao parkFreespaceDao;

    @Override
    public String report(DataCenterBaseRequest dataCenterBaseRequest, Long parkId) {
        ParkStatusRequest parkStatusRequest = DataChangeTools.convert2bean(dataCenterBaseRequest.getBizContent(), ParkStatusRequest.class);

        //参数校验
        verifyParams(parkStatusRequest);

        /**
         * 开始处理业务
         */
        Long nowSeconds = DateTools.unixTimestamp();
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setParkId(parkId);
        heartbeat.setServerTime(nowSeconds);
        heartbeat.setLocalTime(parkStatusRequest.getLocalTime());
        heartbeat.setChannelId(parkStatusRequest.getChannelId());
        // 判断心跳记录是否存在
        Heartbeat lastHeartbeat = heartbeatDao.getLast(parkId);
        if (lastHeartbeat == null) {
            // 新增心跳表
            heartbeatDao.insertStatusInfo(heartbeat);
        } else {
            // 更新心跳表
            heartbeatDao.updateStatus(heartbeat);
            // 判断上次心跳同步时间与本次间隔
            Long lastServerTime = lastHeartbeat.getServerTime();
            if (lastServerTime + TimeOutConstants.OFF_LINE_TIME < nowSeconds) {
                // 添加车场连接断开记录表
                HeartbeatOffline heartbeatOffline = new HeartbeatOffline();
                heartbeatOffline.setParkId(parkId);
                heartbeatOffline.setLastConnectionTime(lastServerTime);
                heartbeatOffline.setReconnectTime(nowSeconds);
                heartbeatOffline.setOffTime(nowSeconds - lastServerTime);
                heartbeatOfflineDao.insertOffLine(heartbeatOffline);
            }
        }
        if (ToolsUtil.isNotNull(parkStatusRequest.getEmptyNumber())){
            // 修改车场空车位
            ParkFreespace parkFreespace = new ParkFreespace();
            parkFreespace.setParkId(parkId);
            parkFreespace.setFreeSpace(parkStatusRequest.getEmptyNumber());
            parkFreespaceDao.updateFreeSpace(parkFreespace);
        }
        // 返回结果
        ParkStatusResponse parkStatusResponse = new ParkStatusResponse();
        parkStatusResponse.setTime(nowSeconds);
        return ResultTools.setResponse(CodeConstants.SUCCESS,
                CodeConstants.getName(CodeConstants.SUCCESS), parkStatusResponse);
    }

}
