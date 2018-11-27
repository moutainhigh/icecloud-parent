package com.icetech.api.transcenter.service.hystrix;

import com.icetech.api.transcenter.service.OssFeignApi;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;

@Component
public class OssFeignApiHystrix implements OssFeignApi {


    @Override
    public void uploadBase64(String base64, String fileName) {

    }

    @Override
    public String getImageUrl(String fileName) {
        return null;
    }

    @Override
    public InputStream getOSS2InputStream(String name) {
        return null;
    }

    @Override
    public void uploadFileStream(byte[] fileStream, String fileName) {

    }

    @Override
    public void uploadFile(File file, String fileName) {

    }

    @Override
    public File getOSS2File(String fileName) {
        return null;
    }
}
