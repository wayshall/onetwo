package org.onetwo.common.apiclient;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
	final private HttpMethod httpMethod;
	private String requestUrl;
	final private Class<?> responseType;
//	private Object requestBody;
	final private Map<String, Object> uriVariables;
	@Getter
	final private Map<String, Object> pathVariables;
	private Consumer<HttpHeaders> headerCallback;
	private Supplier<Object> requestBodySupplier;
	
	@Builder
	public RequestContextData(RequestMethod requestMethod, Map<String, Object> pathVariables, Map<String, Object> uriVariables, Class<?> responseType) {
		super();
		this.httpMethod = HttpMethod.resolve(requestMethod.name());
		this.uriVariables = uriVariables;
		this.pathVariables = pathVariables;
		this.responseType = responseType;
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
	
	public Map<String, Object> getUriVariables() {
		return uriVariables;
	}
	
	public Supplier<Object> getRequestBodySupplier() {
		return requestBodySupplier;
	}

	public RequestContextData requestBodySupplier(Supplier<Object> requestBodySupplier) {
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

}
