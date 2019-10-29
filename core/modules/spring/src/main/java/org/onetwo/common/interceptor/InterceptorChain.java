package org.onetwo.common.interceptor;
/**
 * @author weishao zeng
 * <br/>
 */
public interface InterceptorChain {

	Object invoke() throws Throwable ;
}

