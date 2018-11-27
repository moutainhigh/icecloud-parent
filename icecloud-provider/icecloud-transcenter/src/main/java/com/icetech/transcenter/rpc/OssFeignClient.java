package com.icetech.transcenter.rpc;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.icetech.api.transcenter.service.OssFeignApi;
import com.icetech.common.Base64Tools;
import com.icetech.transcenter.config.oss.OssConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

@RestController
public class OssFeignClient implements OssFeignApi {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OssConfig ossConfig;

    @Override
    public void uploadBase64(String base64, String fileName) {
        byte[] file = Base64Tools.decode2Byte(base64);
        uploadFileStream(file, fileName);
    }
    @Override
    public String getImageUrl(String name){

        String accessKeyId = ossConfig.getAccessKeyId();
        String accessKeySecret = ossConfig.getAccessKeySecret();
        String endpoint = ossConfig.getEndpoint();
        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        String bucketName = ossConfig.getBucketName();
        String key = name;
        // 设置URL过期时间为1小时
        Date expiration = new Date(new Date().getTime() + 3600 * 1000);

        // 生成URL
        URL url = client.generatePresignedUrl(bucketName, key, expiration);

        return url.toString();
    }
    @Override
    public InputStream getOSS2InputStream(String name){
        String accessKeyId = ossConfig.getAccessKeyId();
        String accessKeySecret = ossConfig.getAccessKeySecret();
        String endpoint = ossConfig.getEndpoint();
        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        OSSObject ossObj = client.getObject(ossConfig.getBucketName(), name);
        return ossObj.getObjectContent();
    }
    @Override
    public void uploadFileStream(byte[] fileStream, String fileName){
        String endpoint = ossConfig.getEndpoint();
        String accessKeyId = ossConfig.getAccessKeyId();
        String accessKeySecret = ossConfig.getAccessKeySecret();
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId,accessKeySecret);
        ossClient.putObject(ossConfig.getBucketName(), fileName, new ByteArrayInputStream(fileStream));
        ossClient.shutdown();
    }
    @Override
    public void uploadFile(File file, String fileName){
        String endpoint = ossConfig.getEndpoint();
        String accessKeyId = ossConfig.getAccessKeyId();
        String accessKeySecret = ossConfig.getAccessKeySecret();
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(ossConfig.getBucketName(), fileName, file);
        ossClient.shutdown();
    }
    @Override
    public File getOSS2File(String fileName){
        String endpoint = ossConfig.getEndpoint();
        String accessKeyId = ossConfig.getAccessKeyId();
        String accessKeySecret = ossConfig.getAccessKeySecret();
        String bucketName = ossConfig.getBucketName();
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String path = request.getSession().getServletContext().getRealPath("/WEB-INF/");
        String[] split = fileName.split("/");
        String fileName1 =split[3];
        logger.info("***fileName="+fileName);
        File file = new File(path +"/temporary/");
        if(!(file.exists()) && !(file.isDirectory())){
            boolean mkdirs = file.mkdirs();
            if(mkdirs){
                logger.info("创建文件夹成功 路径:"+path +"temporary/");
            }else {
                logger.info("创建文件夹失败");
            }
        }
        File csvFile = new File(path +"/temporary/"+fileName1);
        logger.info("***csvFile"+csvFile);
        ossClient.getObject(new GetObjectRequest(bucketName, fileName), csvFile);
        ossClient.shutdown();
        return csvFile;
    }
}
