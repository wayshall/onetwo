package org.onetwo.plugins.rest;

import java.lang.reflect.Method;
import java.util.Map;

import org.onetwo.common.proxy.BaseMethodParameter;
import org.onetwo.common.proxy.CacheableDynamicProxyHandler;
import org.onetwo.common.spring.rest.JFishRestTemplate;
import org.onetwo.common.utils.Langs;
import org.onetwo.common.utils.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.google.common.cache.Cache;

public class RestClientHandler extends CacheableDynamicProxyHandler<BaseMethodParameter, RestMethodResolver> {

//	private final Cache<Method, RestMethodResolver> methodCaches;

	final private String baseUrl;
	private RestTemplate restTemplate;
	
	public RestClientHandler(String baseUrl, Cache<Method, RestMethodResolver> methodCaches, Class<?>... proxiedInterfaces) {
		this(baseUrl, null, methodCaches, proxiedInterfaces);
	}
	
	public RestClientHandler(String baseUrl, RestTemplate restTemplate, Cache<Method, RestMethodResolver> methodCaches, Class<?>... proxiedInterfaces) {
		super(methodCaches, proxiedInterfaces);
		this.restTemplate = restTemplate==null?new JFishRestTemplate():restTemplate;
		this.baseUrl = baseUrl;
	}

	@Override
	protected Object invokeMethod(Object proxy, RestMethodResolver method, Object[] args) throws Throwable {
		RequestMethod reqMethod = method.getRequestMethod();
		Map<Object, Object> paramMap = method.toMapByArgs(args);
		ResponseEntity<?> response = null;
		switch (reqMethod) {
			case GET:
				String reqUrl = method.getUrlWithQueryParams(paramMap);
				reqUrl = baseUrl + StringUtils.appendStartWith(reqUrl, "/");
				response = restTemplate.getForEntity(reqUrl, method.getResponseType(), Langs.toArray(paramMap));
				break;
	
			default:
				throw new UnsupportedOperationException(reqMethod.toString());
		}
		
		if(!HttpStatus.OK.equals(response.getStatusCode())){
			throw new RestClientException("invoke rest interface["+method+"] error: " + response);
		}
		if(method.getComponentClass()==ResponseEntity.class){
			return response;
		}
		return response.getBody();
	}

	@Override
	protected RestMethodResolver createMethodResolver(Method method) {
		return new RestMethodResolver(method);
	}


}
