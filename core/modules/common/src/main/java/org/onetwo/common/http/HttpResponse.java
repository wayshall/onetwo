package org.onetwo.common.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.StringUtils;

public class HttpResponse {
	public static final String DEFAULT_ENCODE = "UTF-8";

	private HttpURLConnection httpConnection;
	private int responseCode;
    private InputStream inputStream;
//    private Document document;
    private String charset = HttpRequest.DEFAULT_ENCODE;
    private String text;
    private boolean streamConsumed = false;
	
	public HttpResponse(HttpURLConnection httpConnection){
		this.httpConnection = httpConnection;
		try {
			this.responseCode = this.httpConnection.getResponseCode();
			
			if(this.responseCode==HttpURLConnection.HTTP_OK)
				inputStream = this.httpConnection.getInputStream();
			else
				inputStream = this.httpConnection.getErrorStream();
			
	        if ("gzip".equals(httpConnection.getContentEncoding())) {
	            // the response is gzipped
	            inputStream = new GZIPInputStream(inputStream);
	        }
	        Map<String, String> contentType = parseContenType(this.httpConnection.getContentType());
	        if(contentType.containsKey("charset"))
	        	charset = contentType.get("charset");
		} catch (IOException e) {
			throw new BaseException("error response!" , e);
		}
	}
	
	protected Map<String, String> parseContenType(String contentType){
        Map<String, String> map = new HashMap<String, String>();
        if(StringUtils.isNotBlank(contentType)){
        	String[] cts = StringUtils.split(contentType, ";");
        	if(cts==null || cts.length==0)
        		return map;
        	int index = 0;
        	for(String str : cts){
        		if(index==0)
        			map.put("type", str);
        		else{
        			String[] pv = StringUtils.split(str, "=");
        			if(pv==null || pv.length<2)
        				continue;
        			if(StringUtils.isBlank(pv[0]))
        				continue;
        			map.put(pv[0].trim(), pv[1]);
        		}
        		index++;
        	}
        }
		return map;
	}
	
	
    public String asString(){
    	return asString(charset);
    }
	
    public String asString(String encode){
    	if(StringUtils.isBlank(encode))
    		encode = HttpRequest.DEFAULT_ENCODE;
        if(text==null){
            BufferedReader br = null;
            try {
                InputStream stream = asInputStream();
                if(stream==null)
                	throw new ServiceException("请求出错！");
                br = new BufferedReader(new InputStreamReader(stream, encode));
                StringBuffer buf = new StringBuffer();
                String line;
                while (null != (line = br.readLine())) {
                    buf.append(line).append("\n");
                }
                this.text = buf.toString();
                stream.close();
            } catch (Exception e) {
                throw new BaseException(e.getMessage(), e);
            }finally{
            	if(br!=null)
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
                httpConnection.disconnect();
                streamConsumed = true;
            }
        }
        return text;
    }

    public String getResponseHeader(String name) {
        return httpConnection.getHeaderField(name);
    }
    
    public void disconnect(){
    	httpConnection.disconnect();
    }

	public HttpURLConnection getHttpConnection() {
		return httpConnection;
	}

	public InputStream asInputStream() {
        if(streamConsumed){
            throw new IllegalStateException("Stream has already been consumed.");
        }
		return inputStream;
	}
}
