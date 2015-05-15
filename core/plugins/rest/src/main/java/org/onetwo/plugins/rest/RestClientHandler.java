package org.onetwo.plugins.rest;

import java.lang.reflect.Method;
import java.util.Map;

import org.onetwo.common.proxy.BaseMethodParameter;
import org.onetwo.common.proxy.CacheableDynamicProxyHandler;
import org.onetwo.common.spring.rest.JFishRestTemplate;
import org.onetwo.common.utils.Langs;
import org.springframework.web.bind.annotation.RequestMethod;

public class RestClientHandler extends CacheableDynamicProxyHandler<BaseMethodParameter, RestMethodResolver> {

//	private final Cache<Method, RestMethodResolver> methodCaches;
	
	private JFishRestTemplate restTemplate;
	
	public RestClientHandler(Class<?>... proxiedInterfaces) {
		this(new JFishRestTemplate(), proxiedInterfaces);
	}
	
	public RestClientHandler(JFishRestTemplate restTemplate, Class<?>... proxiedInterfaces) {
		super(proxiedInterfaces);
		this.restTemplate = restTemplate==null?new JFishRestTemplate():restTemplate;
	}

	@Override
	protected Object invokeMethod(Object proxy, RestMethodResolver method, Object[] args) throws Throwable {
		RequestMethod reqMethod = method.getRequestMethod();
		Map<Object, Object> paramMap = method.toMapByArgs(args);
		Object result = null;
		switch (reqMethod) {
			case GET:
				String reqUrl = method.getUrlWithQueryParams(paramMap);
				result = restTemplate.get(reqUrl, method.getResponseType(), Langs.toArray(paramMap));
				break;
	
			default:
				throw new UnsupportedOperationException(reqMethod.toString());
		}
		return result;
	}

	@Override
	protected RestMethodResolver createMethodResolver(Method method) {
		return new RestMethodResolver(method);
	}


}
