package com.icetech.datacenter.controller;

import com.icetech.api.datacenter.model.request.NoplateEnterRequest;
import com.icetech.api.datacenter.model.request.NoplateExitRequest;
import com.icetech.api.datacenter.model.request.RemoteSwitchRequest;
import com.icetech.api.datacenter.service.RemoteSwitchFeignApi;
import com.icetech.common.DataChangeTools;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.datacenter.rpc.down.NoplateEnterFeignClient;
import com.icetech.datacenter.rpc.down.NoplateExitFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class TestController {

    @Autowired
    private NoplateEnterFeignClient noplateEnterService;

    @RequestMapping("/noplateEnter")
    public String executeReport(String param, HttpServletRequest request) throws IOException {
        request.setAttribute("startTime", System.currentTimeMillis());
        NoplateEnterRequest noplateEnterRequest = new NoplateEnterRequest();
        noplateEnterRequest.setParkCode("P001");
        noplateEnterRequest.setChannelId("C001");
        noplateEnterRequest.setEnterTime(1538123577L);
        noplateEnterRequest.setPlateNum("京A12345");
        return DataChangeTools.bean2gson(noplateEnterService.noplateEnter(noplateEnterRequest));
    }

    @Autowired
    private NoplateExitFeignClient noplateExitService;
    @GetMapping("/noplateExit")
    public ObjectResponse noplateExit(String param,  HttpServletRequest request) throws IOException {
        request.setAttribute("startTime", System.currentTimeMillis());
        NoplateExitRequest noplateEnterRequest = new NoplateExitRequest();
        noplateEnterRequest.setParkCode("P001");
        noplateEnterRequest.setChannelId("C001");
        noplateEnterRequest.setExitTime(1538123677L);
        noplateEnterRequest.setPlateNum("京A12345");
        return noplateExitService.noplateExit(noplateEnterRequest);
    }

    @Autowired
    private RemoteSwitchFeignApi remoteSwitchService;
    @RequestMapping("/remoteSwitch")
    public String remoteSwitch(String param, HttpServletRequest request) throws IOException {
        request.setAttribute("startTime", System.currentTimeMillis());
        RemoteSwitchRequest remoteSwitchRequest = new RemoteSwitchRequest();
        remoteSwitchRequest.setParkCode("P001");
        remoteSwitchRequest.setChannelId("C001");
        remoteSwitchRequest.setDeviceNo("D1538123577");
        remoteSwitchRequest.setSwitchType(1);
        remoteSwitchRequest.setOperAccount("fct");
        remoteSwitchRequest.setSequenceId("1245");
        return DataChangeTools.bean2gson(remoteSwitchService.remoteSwitch(remoteSwitchRequest));
    }
}
