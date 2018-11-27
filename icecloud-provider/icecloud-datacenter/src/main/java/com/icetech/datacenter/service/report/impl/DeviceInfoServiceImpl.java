package com.icetech.datacenter.service.report.impl;

import com.icetech.common.DataChangeTools;
import com.icetech.common.ResultTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.datacenter.dao.ParkDeviceDao;
import com.icetech.datacenter.domain.ParkDevice;
import com.icetech.datacenter.domain.request.DataCenterBaseRequest;
import com.icetech.datacenter.domain.request.DeviceInfoRequest;
import com.icetech.datacenter.service.AbstractService;
import com.icetech.datacenter.service.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceInfoServiceImpl extends AbstractService implements ReportService {

    @Autowired
    private ParkDeviceDao parkDeviceDao;

    //操作类型，1：增加，2：删除，3：修改
    private static final int[] OPER_TYPE = {1,2,3};

    //删除记录的标记
    private static final int DEL_FLAG = 1;

    @Override
    public String report(DataCenterBaseRequest dataCenterBaseRequest, Long parkId) {

        DeviceInfoRequest deviceInfoRequest = DataChangeTools.convert2bean(dataCenterBaseRequest.getBizContent(), DeviceInfoRequest.class);

        //参数校验
        verifyParams(deviceInfoRequest);

        ParkDevice parkDevice_para = new ParkDevice();
        parkDevice_para.setParkId(parkId);
        parkDevice_para.setDeviceNo(deviceInfoRequest.getDeviceNo());
        ParkDevice parkDevice_result = parkDeviceDao.selectById(parkDevice_para);
        int operType = deviceInfoRequest.getOperType();
        if (parkDevice_result != null){
            if (operType == OPER_TYPE[0]){
                return ResultTools.setResponse(CodeConstants.ERROR_405, CodeConstants.getName(CodeConstants.ERROR_405));
            }else if (operType == OPER_TYPE[1]){
                ParkDevice parkDevice = new ParkDevice();
                parkDevice.setParkId(parkId);
                parkDevice.setDeviceNo(deviceInfoRequest.getDeviceNo());
                parkDevice.setDelFlag(DEL_FLAG);
                parkDeviceDao.update(parkDevice);
            }else if (operType == OPER_TYPE[2]){
                parkDeviceDao.update(getParkDevice(deviceInfoRequest, parkId));
            }
        }else{
            if (operType == OPER_TYPE[0]){
                parkDeviceDao.insert(getParkDevice(deviceInfoRequest, parkId));
            }else if (operType == OPER_TYPE[1]){
                return ResultTools.setResponse(CodeConstants.ERROR_405, CodeConstants.getName(CodeConstants.ERROR_405));
            }else if (operType == OPER_TYPE[2]){
                //修改操作，表中无记录时，新增记录
                parkDeviceDao.insert(getParkDevice(deviceInfoRequest, parkId));
            }
        }

        return ResultTools.setResponse(CodeConstants.SUCCESS, CodeConstants.getName(CodeConstants.SUCCESS));
    }

    /**
     * 获取实体对象
     * @param deviceInfoRequest
     * @param parkId
     * @return
     */
    private ParkDevice getParkDevice(DeviceInfoRequest deviceInfoRequest, Long parkId) {
        ParkDevice parkDevice = new ParkDevice();
        parkDevice.setParkId(parkId);
        parkDevice.setDeviceNo(deviceInfoRequest.getDeviceNo());
        parkDevice.setType(deviceInfoRequest.getDeviceType());
//        parkDevice.setChannelId(deviceInfoRequest.getChannelId());
        parkDevice.setIp(deviceInfoRequest.getDeviceIp());
        parkDevice.setPort(deviceInfoRequest.getDevicePort());
        return parkDevice;
    }
}
