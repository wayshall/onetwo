package org.onetwo.common.http;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.StringUtils;

public class HttpRequest {
	
	public static final String REQUEST_AUTHORIZATION = "Authorization";
	public static final String AUTHORIZATION_BASIC = "Basic";
	
	public static final String POST = "POST";
	public static final String GET = "GET";
	public static final String DEFAULT_ENCODE = "UTF-8";
	public static final String ENCODE_KEY = "HttpRequest_encode";

	private String encode = DEFAULT_ENCODE;
	private String url;
	private Map<String, String> headers = new HashMap<String, String>();
	private List<HttpParam> params;
	private HttpURLConnection httpConnection;
	private int connectionTimeout = 0;
	private int readTimeout = 0;
	private String method;
	private boolean execute;
	
	private String basic;
	private HttpProxy proxy;

	public HttpRequest(String url){
		this(url, new ArrayList<HttpParam>());
	}
	
	public HttpRequest(String url, List<HttpParam> params){
		this.url = url;
		this.params = params;
	}

	public void setProxy(HttpProxy proxy) {
		this.proxy = proxy;
	}

	public void setProxy(String host, int port) {
		HttpProxy p = new HttpProxy(host, port);
		this.proxy = p;
	}
	
	public void get(){
		this.openConnection();
		this.method = GET;
		try {
			this.httpConnection.setRequestMethod(method);
		} catch (ProtocolException e) {
			throw new BaseException("get connection error:"+e.getMessage(), e);
		}
	}
	
	public void post(){
		this.openConnection();
		this.method = POST;
		OutputStream os = null;
		try {
			httpConnection.setRequestMethod(method);
			httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpConnection.setDoOutput(true);
			
			String paramString = this.getParamString();
			byte[] bytes = paramString.getBytes(encode);
			httpConnection.setRequestProperty("Content-Length", Integer.toString(bytes.length));
			
			os = httpConnection.getOutputStream();
			os.write(bytes);
			os.flush();
		} catch (Exception e) {
			throw new BaseException("post connection error:"+e.getMessage(), e);
		} finally{
			if(os!=null)
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	protected void openConnection(){
		try {
			if(useProxy()){
				if(proxy.getAuthenticator()!=null)
					Authenticator.setDefault(proxy.getAuthenticator());
				httpConnection = (HttpURLConnection) new URL(url).openConnection(proxy.getProxy());
			}else{
				httpConnection = (HttpURLConnection) new URL(url).openConnection();
			}
			
			if(connectionTimeout>0)
				httpConnection.setConnectTimeout(connectionTimeout);
			if(readTimeout>0)
				httpConnection.setReadTimeout(readTimeout);
			
			httpConnection.setDoOutput(true);
			if(StringUtils.isNotBlank(basic)){
				httpConnection.setRequestProperty(REQUEST_AUTHORIZATION, basic);
			}
			if(!this.headers.isEmpty()){
				for(Map.Entry<String, String> entry : this.headers.entrySet()){
					httpConnection.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			execute = true;
		} catch (Exception e) {
			throw new BaseException("open connection error : " + e.getMessage(), e);
		}
	}
	
	protected String getParamString(){
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for(HttpParam param : params){
			if(index!=0)
				sb.append("&");
			try {
				sb.append(URLEncoder.encode(param.getName(), encode)).append("=").append(URLEncoder.encode(param.getValue(), encode));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			index++;
		}
		return sb.toString();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<HttpParam> getParams() {
		return params;
	}

	public void setParams(List<HttpParam> params) {
		this.params = params;
	}

	public HttpRequest addParam(String name, String value) {
		HttpParam p = new HttpParam(name, value);
		if(!params.contains(p))
			params.add(p);
		return this;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public HttpURLConnection getHttpConnection() {
		return httpConnection;
	}

	public String getMethod() {
		return method;
	}

	public boolean isExecute() {
		return execute;
	}

	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
		if(StringUtils.isBlank(encode))
			setContentType(null, encode);
	}
	
	public void setContentType(String type, String charset){
		if(StringUtils.isBlank(type))
			type = "text/html";
		if(StringUtils.isBlank(charset))
			charset = DEFAULT_ENCODE;
		getHeaders().put("contentType", MyUtils.append(type, ";", charset));
	}
	
	public boolean useProxy(){
		return proxy!=null;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public HttpRequest addHeader(String key, String value) {
		this.headers.put(key, value);
		return this;
	}

	public String getBasic() {
		return basic;
	}

	public void setBasic(String basic) {
		this.basic = basic;
	}

	public void setAuth(String authUser, String authPassword) {
		this.basic = generatedBasic(authUser, authPassword);
	}
	
	public static String generatedBasic(String authUser, String authPassword){
		String basic = null;
		if(StringUtils.isNotBlank(authUser) && StringUtils.isNotBlank(authPassword)){
			StringBuilder sb = new StringBuilder(AUTHORIZATION_BASIC);
			sb.append(" ").append(Base64.encodeBase64(LangUtils.getBytes(authUser+":"+authPassword)));
			basic = sb.toString();
		}
		return basic;
	}

}
