package com.icetech.paycenter.domain.normalpay.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class TransactionDetailsDownloadRequest implements Serializable {
    @JSONField(deserialize = false,serialize = false)
    private String parkCode;
    private String platformId;
    private String slcTransDate;
    private String segmentIndex;
    private String fileType;
    private String segmentSize;
    private String reserve;
}
