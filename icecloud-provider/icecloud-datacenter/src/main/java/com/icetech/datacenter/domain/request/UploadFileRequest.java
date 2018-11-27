package com.icetech.datacenter.domain.request;


import com.icetech.common.annotation.NotNull;

public class UploadFileRequest {
    @NotNull
    private Integer fileType;
    @NotNull
    private String fileName;
    @NotNull
    private String base64Str;

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getBase64Str() {
        return base64Str;
    }

    public void setBase64Str(String base64Str) {
        this.base64Str = base64Str;
    }
}
