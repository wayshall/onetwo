package org.springframework.cloud.netflix.feign;

import java.util.function.Supplier;

import org.onetwo.cloud.feign.FeignProperties;
import org.onetwo.cloud.feign.FeignProperties.ServiceProps;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author weishao zeng
 * <br/>
 */
public class PropsTargeterEnhancer implements TargeterEnhancer {
	@Autowired
	private FeignProperties feignProperties;

	@Override
	public <T> T enhanceTargeter(FeignClientFactoryBean factory, Supplier<T> defaultTarget) {
		String serviceName = factory.getName();
		if (feignProperties.getServices().containsKey(serviceName)) {
			ServiceProps serviceProp = feignProperties.getServices().get(serviceName);
			factory.setUrl(serviceProp.getUrl());
		}
		return defaultTarget.get();
	}
	
}

