package org.onetwo.common.apiclient;

import java.lang.reflect.Type;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;
import org.onetwo.common.spring.rest.RestUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import com.google.common.collect.Maps;

import lombok.Builder;
import lombok.Getter;

/**
 * @author wayshall
 * <br/>
 */
public class RequestContextData {
	final private String baseURL;
	final private String apiPath;
	
	final private String requestId;
	final private HttpMethod httpMethod;
//	private String requestUrl;
	private Type responseType;
//	private Object requestBody;
	private Map<String, Object> uriVariables;
	/***
	 * 这里在判断是否queryString参数时，遵循浏览器规则:
	 * 1. get和delete请求时，只要不是url变量和特定参数，都当做queryString参数处理
	 * 2. get和delete请求时，只要不是url变量和特定参数，都当做queryString参数处理
	 */
	@Getter
	final private Map<String, Object> queryParameters;
//	private Consumer<HttpHeaders> headerCallback;
//	private ApiRequestCallback apiRequestCallback;
//	private RequestBodySupplier requestBodySupplier;
	private Object requestBody;
	
	private Object[] methodArgs;
	
	private ApiClientMethod invokeMethod;
	@Getter
	private MethodInvocation invocation;
	@Getter
	private int invokeCount = 0;
	@Getter
	final protected int maxRetryCount;
	
	private HttpHeaders headers = new HttpHeaders();
	
	private ApiClientMethodConfig apiClientMethodConfig;
	
//	private ApiBeforeExecuteCallback beforeExecuteCallback;
	
	@Builder
	public RequestContextData(String requestId, String httpMethod, 
							Map<String, Object> queryParameters, 
							Map<String, Object> uriVariables,
							Type responseType, Object[] methodArgs, ApiClientMethod invokeMethod, 
							MethodInvocation invocation, int maxRetryCount, String baseURL, String apiPath) {
		super();
		this.httpMethod = HttpMethod.valueOf(httpMethod);
		this.queryParameters = queryParameters;
		if (uriVariables==null) {
			this.uriVariables = queryParameters;
		} else {
			this.uriVariables = uriVariables;
		}
		this.responseType = responseType;
		this.requestId = requestId;
		this.methodArgs = methodArgs;
		this.invokeMethod = invokeMethod;
		this.invocation = invocation;
		this.maxRetryCount = maxRetryCount;
		this.baseURL = baseURL;
		this.apiPath = apiPath;
	}
	
	public boolean isRetryable() {
		return invokeCount-1 < maxRetryCount;
	}
	
	public void increaseInvokeCount(int times) {
		this.invokeCount = this.invokeCount + times;
	}
	
	public Type getResponseType() {
		return responseType;
	}

	public HttpMethod getHttpMethod() {
		return httpMethod;
	}
	
	
	public Map<String, Object> getUriVariables() {
		Map<String, Object> map = Maps.newHashMap(uriVariables);
		map.putAll(getQueryParameters());
		return map;
	}
	
	/*public RequestBodySupplier getRequestBodySupplier() {
		return requestBodySupplier;
	}

	public RequestContextData requestBodySupplier(RequestBodySupplier requestBodySupplier) {
		this.requestBodySupplier = requestBodySupplier;
		return this;
	}*/
	

	public Object getRequestBody() {
		return requestBody;
	}
	public void setRequestBody(Object requestBody) {
		this.requestBody = requestBody;
	}
//	public RequestContextData headerCallback(Consumer<HttpHeaders> headerCallback) {
//		this.headerCallback = headerCallback;
//		return this;
//	}
//	public RequestContextData apiRequestCallback(ApiRequestCallback apiRequestCallback) {
//		this.apiRequestCallback = apiRequestCallback;
//		return this;
//	}
	
	/*public Consumer<HttpHeaders> getHeaderCallback() {
		return headerCallback;
	}*/
//	public boolean hasApiRequestCallback() {
//		return apiRequestCallback!=null;
//	}

//	public void acceptHeaderCallback() {
//		if (this.headerCallback!=null) {
//			this.headerCallback.accept(headers);
//		}
//	}
//	public void acceptRequestCallback(ClientHttpRequest request) {
//		if (this.apiRequestCallback!=null) {
//			this.apiRequestCallback.doWithRequest(request);
//		} else {
//			// log no header callback
//		}
//	}
	
	@Override
	public String toString() {
		return "RequestContextData [httpMethod=" + httpMethod
				+ ", requestUrl=" + getRequestUrl() + ", responseType="
				+ responseType + ", uriVariables=" + uriVariables + ", requestCallback="
//				+ apiRequestCallback 
				+ "]";
	}
	
	public static interface RequestBodySupplier {
		Object getRequestBody(RequestContextData context);
	}

	public String getRequestId() {
		return requestId;
	}

	public Object[] getMethodArgs() {
		return methodArgs;
	}

	public void setResponseType(Class<?> responseType) {
		this.responseType = responseType;
	}

	public ApiClientMethod getInvokeMethod() {
		return invokeMethod;
	}

	public HttpHeaders getHeaders() {
		return headers;
	}

	public void setUriVariables(Map<String, Object> uriVariables) {
		this.uriVariables = uriVariables;
	}
	
	
	/****
	 * 解释pathvariable参数，并且把所有queryParameters转化为queryString参数
	 * @author wayshall
	 * @param invokeMethod
	 * @param context
	 * @return
	 */
	public String getRequestUrl(){
		String actualUrl = RestUtils.concatPath(baseURL, apiPath, invokeMethod.getApiRequestPath(this.methodArgs));
		actualUrl = RestUtils.appendQueryParametersToURL(actualUrl, this.getQueryParameters());
		return actualUrl;
	}

	public ApiClientMethodConfig getApiClientMethodConfig() {
		return apiClientMethodConfig;
	}

	public void setApiClientMethodConfig(ApiClientMethodConfig apiClientMethodConfig) {
		this.apiClientMethodConfig = apiClientMethodConfig;
	}
	
//	public void setRequestUrl(String url) {
//		this.requestUrl = url;
//	}
//	public String getRequestUrl() {
//		return requestUrl;
//	}
	
//	static public interface ApiBeforeExecuteCallback {
//		
//		void apply();
//
//	}
	
//	public RequestContextData beforeExecuteCallback(ApiBeforeExecuteCallback beforeExecuteCallback) {
//		this.beforeExecuteCallback = beforeExecuteCallback;
//		return this;
//	}
//	public void applyBeforeExecuteCallback() {
//		if (beforeExecuteCallback!=null) {
//			beforeExecuteCallback.apply();
//		}
//	}

}
