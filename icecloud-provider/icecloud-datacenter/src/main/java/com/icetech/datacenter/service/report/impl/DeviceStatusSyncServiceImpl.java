package com.icetech.datacenter.service.report.impl;

import com.icetech.common.*;
import com.icetech.common.constants.CodeConstants;
import com.icetech.datacenter.dao.ParkDeviceDao;
import com.icetech.datacenter.dao.ParkDevrecordDao;
import com.icetech.datacenter.domain.ParkDevice;
import com.icetech.datacenter.domain.ParkDevrecord;
import com.icetech.datacenter.domain.request.DataCenterBaseRequest;
import com.icetech.datacenter.domain.request.DeviceStatusRequest;
import com.icetech.datacenter.service.AbstractService;
import com.icetech.datacenter.service.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DeviceStatusSyncServiceImpl extends AbstractService implements ReportService {

    @Autowired
    private ParkDevrecordDao parkDevrecordDao;
    @Autowired
    private ParkDeviceDao parkDeviceDao;

    //设备类型，1：在线，2：离线，3：故障
    private static final int[] DEVICE_STATUS = {1,2,3};

    @Override
    public String report(DataCenterBaseRequest dataCenterBaseRequest, Long parkId) throws Exception {

        DeviceStatusRequest deviceStatusRequest = DataChangeTools.convert2bean(dataCenterBaseRequest.getBizContent(), DeviceStatusRequest.class);

        //参数校验
        verifyParams(deviceStatusRequest);

        ParkDevice parkDevice_para = new ParkDevice();
        parkDevice_para.setParkId(parkId);
        parkDevice_para.setDeviceNo(deviceStatusRequest.getDeviceNo());
        ParkDevice parkDevice_result = parkDeviceDao.selectById(parkDevice_para);
        if (parkDevice_result != null){
            parkDeviceDao.update(getUpdatePara(deviceStatusRequest, parkId));
            if (deviceStatusRequest.getDeviceStatus() != DEVICE_STATUS[0]){
                parkDevrecordDao.insert(getInsertPara(deviceStatusRequest, parkDevice_result.getId()));
            }
        }else{
            return ResultTools.setResponse(CodeConstants.ERROR_404, CodeConstants.getName(CodeConstants.ERROR_404));
        }

        return ResultTools.setResponse(CodeConstants.SUCCESS, CodeConstants.getName(CodeConstants.SUCCESS));
    }

    /**
     * 获取更新时的实体对象
     * @param deviceStatusRequest
     * @param parkId
     * @return
     */
    private ParkDevice getUpdatePara(DeviceStatusRequest deviceStatusRequest, Long parkId) {
        ParkDevice parkDevice = new ParkDevice();
        parkDevice.setParkId(parkId);
        parkDevice.setDeviceNo(deviceStatusRequest.getDeviceNo());
        parkDevice.setStatus(deviceStatusRequest.getDeviceStatus());
        return parkDevice;
    }

    /**
     * 获取更新时的实体对象
     * @param deviceStatusRequest
     * @param deviceId
     * @return
     */
    private ParkDevrecord getInsertPara(DeviceStatusRequest deviceStatusRequest, Long deviceId) {
        ParkDevrecord parkDevrecord = new ParkDevrecord();
        parkDevrecord.setDeviceId(deviceId);
        parkDevrecord.setStatus(deviceStatusRequest.getDeviceStatus());
        parkDevrecord.setReason(deviceStatusRequest.getFailureCause());
        parkDevrecord.setWrongTime(DateTools.getFormat(new Date()));
        return parkDevrecord;
    }
}
