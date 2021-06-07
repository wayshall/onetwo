package org.springframework.cloud.openfeign;

import org.springframework.cloud.openfeign.ExtTargeter.FeignTargetContext;

/**
 * @author weishao zeng
 * <br/>
 */
public interface TargeterEnhancer {

	<T> T enhanceTargeter(FeignTargetContext<T> ctx);
	
}

