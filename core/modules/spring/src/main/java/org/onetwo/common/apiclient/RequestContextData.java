package org.onetwo.common.apiclient;

import java.util.Map;
import java.util.function.Consumer;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
	final private Map<String, ?> uriVariables;
	/***
	 * 这里在判断是否queryString参数时，遵循浏览器规则:
	 * 1. get和delete请求时，只要不是url变量和特定参数，都当做queryString参数处理
	 * 2. get和delete请求时，只要不是url变量和特定参数，都当做queryString参数处理
	 */
	@Getter
	final private Map<String, ?> queryParameters;
	private Consumer<HttpHeaders> headerCallback;
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
	
	@Builder
	public RequestContextData(String requestId, RequestMethod requestMethod, 
							Map<String, ?> queryParameters, Map<String, ?> uriVariables, 
							Class<?> responseType, Object[] methodArgs, ApiClientMethod invokeMethod, 
							MethodInvocation invocation, int maxRetryCount) {
		super();
		this.httpMethod = HttpMethod.resolve(requestMethod.name());
		this.uriVariables = uriVariables;
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
	public RequestContextData headerCallback(Consumer<HttpHeaders> headerCallback) {
		this.headerCallback = headerCallback;
		return this;
	}
	/*public Consumer<HttpHeaders> getHeaderCallback() {
		return headerCallback;
	}*/
	public boolean hasHeaderCallback() {
		return headerCallback!=null;
	}

	public void acceptHeaderCallback() {
		acceptHeaderCallback(headers);
	}
	public void acceptHeaderCallback(HttpHeaders headers) {
		if (this.headerCallback!=null) {
			this.headerCallback.accept(headers);
		} else {
			// log no header callback
		}
	}
	
	@Override
	public String toString() {
		return "RequestContextData [httpMethod=" + httpMethod
				+ ", requestUrl=" + requestUrl + ", responseType="
				+ responseType + ", uriVariables=" + uriVariables + ", requestCallback="
				+ headerCallback + "]";
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

}
