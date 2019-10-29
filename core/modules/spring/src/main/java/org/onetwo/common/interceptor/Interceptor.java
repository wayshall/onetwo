package org.onetwo.common.interceptor;

/**
 * @author weishao zeng
 * <br/>
 */
public interface Interceptor {
	
	Object intercept(InterceptorChain chain);

}

