package com.icetech.datacenter.service.report.impl;

import com.icetech.api.transcenter.service.OssFeignApi;
import com.icetech.common.DataChangeTools;
import com.icetech.common.DateTools;
import com.icetech.common.ResultTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.datacenter.domain.request.DataCenterBaseRequest;
import com.icetech.datacenter.domain.request.UploadFileRequest;
import com.icetech.datacenter.service.AbstractService;
import com.icetech.datacenter.service.report.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UploadFileServiceImpl extends AbstractService implements ReportService {
    // 日志管理
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OssFeignApi ossService;

    @Override
    public String report(DataCenterBaseRequest dataCenterBaseRequest, Long parkId) {

        UploadFileRequest uploadFileRequest = DataChangeTools.convert2bean(dataCenterBaseRequest.getBizContent(), UploadFileRequest.class);

        verifyParams(uploadFileRequest);

        String parkCode = dataCenterBaseRequest.getParkCode();

        String date = DateTools.getFormat(DateTools.DF_, new Date());
        String[] ymd = date.split("-");

        int fileType = uploadFileRequest.getFileType();
        String imgFileName = parkCode + "/" + (fileType == 1 ? "image" : "bill")
                + "/" + ymd[0] + ymd[1] + "/" + ymd[2] + "/"
                + uploadFileRequest.getFileName();
        ossService.uploadBase64(uploadFileRequest.getBase64Str(), imgFileName);
        logger.info("<文件上传接口> 文件上传完成，文件名：{}", imgFileName);

        return ResultTools.setResponse(CodeConstants.SUCCESS, CodeConstants.getName(CodeConstants.SUCCESS));
    }
}
