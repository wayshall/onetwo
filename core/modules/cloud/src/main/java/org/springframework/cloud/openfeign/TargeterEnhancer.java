package org.springframework.cloud.netflix.feign;

import org.springframework.cloud.netflix.feign.ExtTargeter.FeignTargetContext;

/**
 * @author weishao zeng
 * <br/>
 */
public interface TargeterEnhancer {

	<T> T enhanceTargeter(FeignTargetContext<T> ctx);
	
}

