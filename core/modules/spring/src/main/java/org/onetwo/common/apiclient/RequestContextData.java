package org.onetwo.common.apiclient;

import java.util.Map;
import java.util.function.Consumer;

import lombok.Builder;
import lombok.Getter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author wayshall
 * <br/>
 */
public class RequestContextData {
	final private String requestId;
	final private HttpMethod httpMethod;
	private String requestUrl;
	final private Class<?> responseType;
//	private Object requestBody;
	final private Map<String, ?> uriVariables;
	@Getter
	final private Map<String, ?> queryParameters;
	private Consumer<HttpHeaders> headerCallback;
	private RequestBodySupplier requestBodySupplier;
	
	@Builder
	public RequestContextData(String requestId, RequestMethod requestMethod, Map<String, ?> queryParameters, Map<String, ?> uriVariables, Class<?> responseType) {
		super();
		this.httpMethod = HttpMethod.resolve(requestMethod.name());
		this.uriVariables = uriVariables;
		this.queryParameters = queryParameters;
		this.responseType = responseType;
		this.requestId = requestId;
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
	
	public RequestBodySupplier getRequestBodySupplier() {
		return requestBodySupplier;
	}

	public RequestContextData requestBodySupplier(RequestBodySupplier requestBodySupplier) {
		this.requestBodySupplier = requestBodySupplier;
		return this;
	}

	/*public Object getRequestBody() {
		return requestBody;
	}
	public void setRequestBody(Object requestBody) {
		this.requestBody = requestBody;
	}*/
	public RequestContextData doWithHeaderCallback(Consumer<HttpHeaders> headerCallback) {
		this.headerCallback = headerCallback;
		return this;
	}
	public Consumer<HttpHeaders> getHeaderCallback() {
		return headerCallback;
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

}
