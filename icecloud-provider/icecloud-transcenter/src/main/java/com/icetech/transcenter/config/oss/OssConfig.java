package com.icetech.transcenter.config.oss;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("ossConfig")
public class OssConfig {
    @Value("${transcenter.oss.bucketName}")
    private String bucketName;
    @Value("${transcenter.oss.endpoint}")
    private String endpoint;
    @Value("${transcenter.oss.accessKeyId}")
    private String accessKeyId;
    @Value("${transcenter.oss.accessKeySecret}")
    private String accessKeySecret;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }
}
