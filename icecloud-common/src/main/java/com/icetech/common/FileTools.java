package com.icetech.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class FileTools {
    
    /** 
     * 读取zip文件中制定文件的内容 
     * @param zipFile 目标zip文件对象 
     * @param readFileName 目标读取文件名字 
     * @return 文件内容 
     * @throws ZipException 
     * @throws IOException 
     */  
    @SuppressWarnings("unchecked")  
    public static List<String[]> getZipFileContent(File zipFile, String readFileName) throws ZipException, IOException {  
    	List<String[]> resultList = new ArrayList<String[]>();
    	
        ZipFile zip = new ZipFile(zipFile, Charset.forName("GBK"));  
        Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();  
          
        ZipEntry ze;  
        // 枚举zip文件内的文件/  
        while (entries.hasMoreElements()) {  
            ze = entries.nextElement();  
            // 读取目标对象  
            if (ze.getName().contains(readFileName)) {  
                Scanner scanner = new Scanner(zip.getInputStream(ze),"GBK");  
                while (scanner.hasNextLine()) {  
                	String line = scanner.nextLine();
                	//System.out.println(new String(line));
                	String[] lineArr = line.split(",");
                	resultList.add(lineArr);
                }  
                scanner.close();  
            }  
        }  
        zip.close();  
          
        return resultList;  
    } 
    /** 
     * 从网络Url中下载文件 
     * @param urlStr 
     * @throws IOException
     */  
    public static byte[] downLoadFromUrl(String urlStr) throws IOException{  
        URL url = new URL(urlStr);   
      //add by fangct on 20170815 设置代理服务
    	//Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyConstants.ProxyHost, ProxyConstants.ProxyPort));
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();    
                //设置超时间为3秒  
        //conn.setConnectTimeout(3*1000);  
        //防止屏蔽程序抓取而返回403错误  
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");  
  
        //得到输入流  
        InputStream inputStream = conn.getInputStream();    
        //获取自己数组  
        byte[] getData = readInputStream(inputStream);      
  
        if(inputStream!=null){  
            inputStream.close();  
        }  
  
        System.out.println("info:"+url+" download success");   
        return getData;
  
    }  
  
  
  
    /** 
     * 从输入流中获取字节数组 
     * @param inputStream 
     * @return 
     * @throws IOException 
     */  
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {    
        byte[] buffer = new byte[1024];    
        int len = 0;    
        ByteArrayOutputStream bos = new ByteArrayOutputStream();    
        while((len = inputStream.read(buffer)) != -1) {    
            bos.write(buffer, 0, len);    
        }    
        bos.close();    
        return bos.toByteArray();    
    }    
        
}
