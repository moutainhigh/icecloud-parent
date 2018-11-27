package com.icetech.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;


public class HttpsProxy {
	private static final String METHOD_POST = "POST";  
    private static final String DEFAULT_CHARSET = "utf-8";
    private static final int COOENT_TIMEOUT = 20000;
    private static final int READ_TIMEOUT = 20000;
    static Logger logger = LoggerFactory.getLogger(HttpsProxy.class);
      
    public static String doPost(String url, String params) throws Exception {  
        String ctype = "application/json;charset=" + DEFAULT_CHARSET;  
        byte[] content = {};  
        if(params != null){  
            content = params.getBytes(DEFAULT_CHARSET);  
        }  
          
        return doPost(url, ctype, content, COOENT_TIMEOUT, READ_TIMEOUT);  
    }  
    public static String doPost(String url, String ctype, byte[] content,
    		int connectTimeout,int readTimeout) throws Exception {  
        HttpsURLConnection conn = null;  
        OutputStream out = null;  
        String rsp = null;  
        try {  
            try{  
                SSLContext ctx = SSLContext.getInstance("TLS");  
                ctx.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()},
                		new SecureRandom());  
                SSLContext.setDefault(ctx);  
  
                conn = getConnection(new URL(url), METHOD_POST, ctype);   
                conn.setHostnameVerifier(new HostnameVerifier() {
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
				}); 
                conn.setConnectTimeout(connectTimeout);  
                conn.setReadTimeout(readTimeout);  
            }catch(Exception e){  
                logger.error("GET_CONNECTOIN_ERROR, URL = " + url, e);  
                throw e;  
            }  
            try{  
                out = conn.getOutputStream();  
                out.write(content);  
                rsp = getResponseAsString(conn);  
            }catch(IOException e){  
            	logger.error("REQUEST_RESPONSE_ERROR, URL = " + url, e);  
                throw e;  
            }  
              
        }finally {  
            if (out != null) {  
                out.close();  
            }  
            if (conn != null) {  
                conn.disconnect();  
            }  
        }  
          
        return rsp;  
    }  
    private static class DefaultTrustManager implements X509TrustManager {
    	  
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}  
  
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}  
  
        public X509Certificate[] getAcceptedIssuers() {  
            return null;  
        }  
  
    }  
      
    private static HttpsURLConnection getConnection(URL url, String method, String ctype)  
            throws IOException {  
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();  
        conn.setRequestMethod(method);  
        conn.setDoInput(true);  
        conn.setDoOutput(true);  
        conn.setRequestProperty("Accept", "text/xml,text/javascript,text/html");  
        conn.setRequestProperty("User-Agent", "stargate");  
        conn.setRequestProperty("Content-Type", ctype);  
        return conn;  
    }  
  
    protected static String getResponseAsString(HttpURLConnection conn) throws IOException {  
        String charset = getResponseCharset(conn.getContentType());  
        InputStream es = conn.getErrorStream();  
        if (es == null) {  
            return getStreamAsString(conn.getInputStream(), charset);  
        } else {  
            String msg = getStreamAsString(es, charset);  
            if (msg==null||"".equals(msg)) {  
                throw new IOException(conn.getResponseCode() + ":" + conn.getResponseMessage());  
            } else {  
                throw new IOException(msg);  
            }  
        }  
    }  
  
    private static String getStreamAsString(InputStream stream, String charset) throws IOException {  
        try {  
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));  
            StringWriter writer = new StringWriter();  
  
            char[] chars = new char[256];  
            int count = 0;  
            while ((count = reader.read(chars)) > 0) {  
                writer.write(chars, 0, count);  
            }  
  
            return writer.toString();  
        } finally {  
            if (stream != null) {  
                stream.close();  
            }  
        }  
    }  
  
    private static String getResponseCharset(String ctype) {  
        String charset = DEFAULT_CHARSET;  
  
        if (ctype!=null&&!"".equals(ctype)) {  
            String[] params = ctype.split(";");  
            for (String param : params) {  
                param = param.trim();  
                if (param.startsWith("charset")) {  
                    String[] pair = param.split("=", 2);  
                    if (pair.length == 2) {  
                        if (pair[1]!=null&&!"".equals(pair[1])) {  
                            charset = pair[1].trim();  
                        }  
                    }  
                    break;  
                }  
            }  
        }  
  
        return charset;  
    }
    public static void main(String[] args) {
    	addpark();

	}
	
    private static void addpark(){
		String url = "https://120.76.75.164:8443/unionapi/park/addpark";
//		String url = "https://123.59.42.34/unionapi/park/addpark";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("park_id", "100033");
		paramMap.put("name", "北京上地三街9号院停车场");
		paramMap.put("address", "北京上地三街9号");
		paramMap.put("phone", "13899884433");
		paramMap.put("lng", "123.945839");
		paramMap.put("lat", "32.464176");
		paramMap.put("total_plot", "90");
		paramMap.put("empty_plot", "56");
		paramMap.put("union_id", "200099");
		paramMap.put("server_id", "800004");
		paramMap.put("rand", Math.random()+"");
		String ret = "";
		try {
			System.err.println(paramMap);
			String linkParams = DataChangeTools.createLinkString(paramMap);
			System.err.println(linkParams);
			String sign =MD5encryptTool.getMD5(linkParams+"key=C9D76F91791B2B48");
			System.err.println(sign);
			paramMap.put("sign", sign);
			//param = DesUtils.encrypt(param,"NQ0eSXs720170114");
			String param = DataChangeTools.bean2gson(paramMap);
			System.err.println(param);
			ret = HttpsProxy.doPost(url, param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.err.println(ret);
	}
}
