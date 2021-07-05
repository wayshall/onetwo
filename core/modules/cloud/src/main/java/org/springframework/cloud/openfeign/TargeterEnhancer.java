package org.springframework.cloud.openfeign;

/**
 * @author weishao zeng
 * <br/>
 */
public interface TargeterEnhancer {

	<T> T enhanceTargeter(FeignTargetContext<T> ctx);
	
}

