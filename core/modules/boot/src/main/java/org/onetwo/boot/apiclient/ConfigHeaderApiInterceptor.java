package org.onetwo.boot.apiclient;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.boot.apiclient.ApiClientConfiguration.ApiClientProperties;
import org.onetwo.common.apiclient.RequestContextData;
import org.onetwo.common.apiclient.interceptor.ApiInterceptor;
import org.onetwo.common.apiclient.interceptor.ApiInterceptorChain;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author weishao zeng
 * <br/>
 */
public class ConfigHeaderApiInterceptor implements ApiInterceptor {
	@Autowired
	private ApiClientProperties clientProperties;
	
	@Override
	public Object intercept(ApiInterceptorChain chain) throws Throwable {
		RequestContextData ctx = (RequestContextData) chain.getRequestContext();

		clientProperties.getHeaders().forEach((name, value) -> {
			if (StringUtils.isNotBlank(value)) {
				ctx.getHeaders().add(name, value);
			}
		});

		return chain.invoke();
	}
	
}
