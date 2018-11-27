package com.icetech.cloudcenter.controller;

import com.icetech.api.cloudcenter.model.request.QueryOrderFeeRequest;
import com.icetech.api.cloudcenter.service.QueryOrderFeeFeginApi;
import com.icetech.common.DataChangeTools;
import com.icetech.common.domain.response.ObjectResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private QueryOrderFeeFeginApi queryOrderFeeService;

    @RequestMapping("/queryOrderFee")
    public String queryOrderFee(){
        QueryOrderFeeRequest queryOrderFeeRequest = new QueryOrderFeeRequest();
        queryOrderFeeRequest.setParkCode("P001");
        queryOrderFeeRequest.setPlateNum("京A12345");
        ObjectResponse objectResponse = queryOrderFeeService.queryOrderFee(queryOrderFeeRequest);
        return DataChangeTools.bean2gson(objectResponse);
    }
}
