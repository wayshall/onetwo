package org.springframework.cloud.netflix.feign;

import java.util.function.Supplier;

/**
 * @author weishao zeng
 * <br/>
 */
public interface TargeterEnhancer {

	<T> T enhanceTargeter(FeignClientFactoryBean factory, Supplier<T> defaultTarget);
	
}

