package com.icetech.datacenter.service.report;

import com.icetech.datacenter.domain.request.DataCenterBaseRequest;

/**
 * Description : 上报的业务接口类
 * @author fangct
 */
public interface ReportService {

    String report(DataCenterBaseRequest dataCenterBaseRequest, Long parkId) throws Exception;
}
