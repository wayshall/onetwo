package org.onetwo.common.apiclient;

import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.Builder;
import lombok.Getter;

/**
 * @author wayshall
 * <br/>
 */
public class RequestContextData {
	final private String requestId;
	final private HttpMethod httpMethod;
	private String requestUrl;
	private Class<?> responseType;
//	private Object requestBody;
	private Map<String, ?> uriVariables;
	/***
	 * 这里在判断是否queryString参数时，遵循浏览器规则:
	 * 1. get和delete请求时，只要不是url变量和特定参数，都当做queryString参数处理
	 * 2. get和delete请求时，只要不是url变量和特定参数，都当做queryString参数处理
	 */
	@Getter
	final private Map<String, Object> queryParameters;
//	private Consumer<HttpHeaders> headerCallback;
	private ApiRequestCallback apiRequestCallback;
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
	
	private ApiBeforeExecuteCallback beforeExecuteCallback;
	
	@Builder
	public RequestContextData(String requestId, RequestMethod requestMethod, 
							Map<String, Object> queryParameters, 
							Class<?> responseType, Object[] methodArgs, ApiClientMethod invokeMethod, 
							MethodInvocation invocation, int maxRetryCount) {
		super();
		this.httpMethod = HttpMethod.resolve(requestMethod.name());
//		this.uriVariables = uriVariables;
		this.queryParameters = queryParameters;
		this.responseType = responseType;
		this.requestId = requestId;
		this.methodArgs = methodArgs;
		this.invokeMethod = invokeMethod;
		this.invocation = invocation;
		this.maxRetryCount = maxRetryCount;
	}
	
	public boolean isRetryable() {
		return invokeCount-1 < maxRetryCount;
	}
	
	public void increaseInvokeCount(int times) {
		this.invokeCount = this.invokeCount + times;
	}
	
	public Class<?> getResponseType() {
		return responseType;
	}

	public HttpMethod getHttpMethod() {
		return httpMethod;
	}
	public void setRequestUrl(String url) {
		this.requestUrl = url;
	}
	public String getRequestUrl() {
		return requestUrl;
	}
	
	public Map<String, ?> getUriVariables() {
		return uriVariables;
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
	public RequestContextData apiRequestCallback(ApiRequestCallback apiRequestCallback) {
		this.apiRequestCallback = apiRequestCallback;
		return this;
	}
	public RequestContextData beforeExecuteCallback(ApiBeforeExecuteCallback beforeExecuteCallback) {
		this.beforeExecuteCallback = beforeExecuteCallback;
		return this;
	}
	public void applyBeforeExecuteCallback() {
		beforeExecuteCallback.apply();
	}
	/*public Consumer<HttpHeaders> getHeaderCallback() {
		return headerCallback;
	}*/
	public boolean hasApiRequestCallback() {
		return apiRequestCallback!=null;
	}

//	public void acceptHeaderCallback() {
//		if (this.headerCallback!=null) {
//			this.headerCallback.accept(headers);
//		}
//	}
	public void acceptRequestCallback(ClientHttpRequest request) {
		if (this.apiRequestCallback!=null) {
			this.apiRequestCallback.doWithRequest(request);
		} else {
			// log no header callback
		}
	}
	
	@Override
	public String toString() {
		return "RequestContextData [httpMethod=" + httpMethod
				+ ", requestUrl=" + requestUrl + ", responseType="
				+ responseType + ", uriVariables=" + uriVariables + ", requestCallback="
				+ apiRequestCallback + "]";
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

	public void setUriVariables(Map<String, ?> uriVariables) {
		this.uriVariables = uriVariables;
	}

}
