package com.icetech.api.transcenter.service;

import com.icetech.api.transcenter.service.hystrix.OssFeignApiHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.File;
import java.io.InputStream;

/**
 * @author wangzw
 */
@FeignClient(value = "icecloud-transcenter", fallback = OssFeignApiHystrix.class)
public interface OssFeignApi {

    /**
     * 上传base64格式的文件
     * @param base64
     * @param fileName
     * @return
     */
    @PostMapping("/api/transcenter/uploadBase64")
    void uploadBase64(String base64, String fileName);
    /**
     * OSS加密图片路径
     * @param fileName
     * @return
     */
    @PostMapping("/api/transcenter/getImageUrl")
    String getImageUrl(String fileName);

    /**
     * 获取图片输入流
     * @param name
     * @return
     */
    @PostMapping("/api/transcenter/getOSS2InputStream")
    InputStream getOSS2InputStream(String name);

    /**
     * byte数组形式上传文件
     * @param fileStream
     * @param fileName
     */
    @PostMapping("/api/transcenter/uploadFileStream")
    void uploadFileStream(byte[] fileStream, String fileName);

    /**
     * 文件对象形式上传文件
     * @param file
     * @param fileName
     */
    @PostMapping("/api/transcenter/uploadFile")
    void uploadFile(File file, String fileName);

    /**
     * 获取文件对象
     * @param fileName
     * @return
     */
    @PostMapping("/api/transcenter/getOSS2File")
    File getOSS2File(String fileName);

}
