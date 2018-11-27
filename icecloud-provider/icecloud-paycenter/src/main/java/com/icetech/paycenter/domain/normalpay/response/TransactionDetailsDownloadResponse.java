package com.icetech.paycenter.domain.normalpay.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class TransactionDetailsDownloadResponse implements Serializable {
    /**
     * 停车场编号
     */
    private String parkCode;
}
