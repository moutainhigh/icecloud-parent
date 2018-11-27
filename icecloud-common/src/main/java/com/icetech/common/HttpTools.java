package com.icetech.common;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpTools {
	
	private static Logger logger = LoggerFactory.getLogger(HttpTools.class);
    /**
     * HttpClient的post请求
     * @param sendurl
     * @param data
     * @return
     */
    public static String HttpClientPost(String sendurl, String data,Map<String, String> map){
    	 // 创建默认的httpClient实例.    
        CloseableHttpClient httpclient = HttpClients.createDefault();  
        //POST的URL  
        HttpPost httppost = new HttpPost(sendurl);
        if (null !=map) {
    		for (Map.Entry<String, String> entry : map.entrySet()) {  
    			httppost.setHeader(entry.getKey(), entry.getValue()); 
        	}  
         }
        //建立一个NameValuePair数组，用于存储欲传送的参数  
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("params", data));  
        UrlEncodedFormEntity uefEntity;  
        HttpEntity entity;
        String result="";
        try {  
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");  
            httppost.setEntity(uefEntity);  
//          RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(20000).setConnectTimeout(20000).build();//设置请求和传输超时时间
//          httppost.setConfig(requestConfig);
            CloseableHttpResponse response = httpclient.execute(httppost);  
            try {  
                entity = response.getEntity();  
                if (entity != null) {  
                	result=EntityUtils.toString(entity, "UTF-8");                  
                } 
            } finally {  
                response.close();  
            }  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (UnsupportedEncodingException e1) {  
            e1.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            // 关闭连接,释放资源    
            try {  
                httpclient.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        } 
        return result;
    }
    /**
     * HttpClient的get请求
     * @param sendurl
     * @param data
     * @return
     */
    public static String HttpClientGet(String sendurl, String data,Map<String, String> map){
    	CloseableHttpClient httpclient = HttpClients.createDefault();  
    	String result="";
        try {  
        	// 创建httpget.
        	HttpGet httpget=null;  
        	if (null==data || "".equals(data)) {
        		httpget = new HttpGet(sendurl); 
			}else {
				httpget = new HttpGet(sendurl+"?"+data); 
			}
        	if (null !=map) {
        		for (Map.Entry<String, String> entry : map.entrySet()) {  
        			httpget.setHeader(entry.getKey(), entry.getValue()); 
        			// logger.info("header 内容："+entry.getKey()+"----"+entry.getValue());
            	}  
             }
        	 logger.info("HttpClientGet请求链接 ：" + httpget.getURI());
             //System.out.println("executing query ：" + httpget.getURI());
//             RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(200).setConnectTimeout(200).build();//设置请求和传输超时时间
//             httpget.setConfig(requestConfig);
             // 执行get请求.    
             CloseableHttpResponse response = httpclient.execute(httpget);  
             try {  
                 // 获取响应实体    
                 HttpEntity entity = response.getEntity();  
                 // 打印响应状态    
                 System.out.println(response.getStatusLine());  
                 if (entity != null) {  
                     result=EntityUtils.toString(entity,"UTF-8");
                 }  
             } finally {  
                 response.close();  
             }  
         } catch (ClientProtocolException e) {  
             e.printStackTrace();  
         } catch (ParseException e) {
             e.printStackTrace();  
         } catch (IOException e) {  
             e.printStackTrace();  
         } finally {  
             // 关闭连接,释放资源    
             try {  
                 httpclient.close();  
             } catch (IOException e) {  
                 e.printStackTrace();  
             }  
         } 
         return result;
    }
    public static final int timeout = 10;
    
    public static String decodeParm(String param)throws UnsupportedEncodingException{
    	param = URLDecoder.decode(param, "UTF-8");
    	return param;
    }
    
    public static String post(String url){
    	return post(url, "");
    }
    
    public static String post(String url, String data){
    	return httpPost(url, data);
    }
    
    public static String post(String url, InputStream instream){
    	try{
    		HttpEntity entity = Request.Post(url).bodyStream(instream, ContentType.create("text/html", Consts.UTF_8)).execute().returnResponse().getEntity();
    		return entity != null ? EntityUtils.toString(entity) : null;
    	}catch (Exception e){
    		e.printStackTrace();
    	}
    	return null;
    }
    
    public static String get(String url){
    	return httpGet(url);
    }
    
    private static String httpPost(String url, String data){
    	try{
    		HttpEntity entity = Request.Post(url).bodyString(data, ContentType.create("text/html", Consts.UTF_8)).execute().returnResponse().getEntity();
    		return entity != null ? EntityUtils.toString(entity) : null;
    	}catch (Exception e){
    		e.printStackTrace();
    	}
    	return null;
    }
    
    public static String postFile(String url, File file){
    	return postFile(url, null, file);
    }
    
    public static String postFile(String url, String name, File file){
    	try{
    		HttpEntity reqEntity = MultipartEntityBuilder.create().addBinaryBody(name, file).build();
    		Request request = Request.Post(url);
    		request.body(reqEntity);
    		HttpEntity resEntity = request.execute().returnResponse().getEntity();
    		return resEntity != null ? EntityUtils.toString(resEntity) : null;
    	}catch (Exception e){
    		e.printStackTrace();
    	}
    	return null;
    }
    
    public static String postJson(String url, String json){
    	try{
    		HttpEntity entity = Request.Post(url).bodyString(json, ContentType.create("application/json", Consts.UTF_8)).execute().returnResponse().getEntity();
    		return entity != null ? EntityUtils.toString(entity) : null;
    	}catch (Exception e){
    		e.printStackTrace();
    	}
    	return null;
    }
    
    public static byte[] getFile(String url){
    	try{
    		Request request = Request.Get(url);
    		HttpEntity resEntity = request.execute().returnResponse().getEntity();
    		return EntityUtils.toByteArray(resEntity);
    	}catch (Exception e){
    		e.printStackTrace();
    	}
    	return null;
    }
    
    private static String httpGet(String url){
      try{
        HttpEntity entity = Request.Get(url).execute().returnResponse().getEntity();
        return entity != null ? EntityUtils.toString(entity, "UTF-8") : null;
      }catch (Exception e){
        e.printStackTrace();
      }
      return null;
    }
    
    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     * @param json
     *            请求参数，请求参数是Json格式。
     * @return 所代表远程资源的响应结果
     * @throws IOException
     * @throws IllegalStateException
     */
    public static String sendPost(String url, String json)
            throws IllegalStateException, IOException {
        // 1. 请求客户端及参数配置
        CloseableHttpClient client = HttpClients.createDefault();
        // 1.3 参数3：请求目标uri
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        int port = uri.getPort();
        if (port == -1) {
            port = 80;// 协议默认端口
        }
        // 2. 获取请求目标，创建请求对象，封装请求参数
        // 2.1 获取请求目标
        HttpHost target = new HttpHost(uri.getHost(), port);
        // 2.2 创建请求对象
        HttpPost request = new HttpPost(uri);
        // 2.3 封装请求参数，设置配置信息
        StringEntity se = new StringEntity(json, "UTF-8");
        se.setContentType("application/json");
        request.setEntity(se);
        // 3. 发送post请求
        CloseableHttpResponse response = null;
        try {
            response = client.execute(target, request);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
                client.close();
            }
        }
        // 4. 读取响应信息
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                response.getEntity().getContent(), "UTF-8"));
        StringBuilder result = new StringBuilder();
        String message = null;
        while ((message = reader.readLine()) != null) {
            result.append(message).append(System.getProperty("line.separator"));
        }
        String resultStr = result.toString();
        // 下边代码移除空白字符
        resultStr = resultStr.trim();
        resultStr = resultStr.replace("\n", "");
        resultStr = resultStr.replace("\r", "");
        resultStr = resultStr.replace("\t", "");
        reader.close();
        return resultStr;
    }

    /**
     * 获取访问来源的IP地址
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress != null && (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1"))) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length() = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

}
