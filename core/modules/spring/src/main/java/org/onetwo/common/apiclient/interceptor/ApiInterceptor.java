package org.onetwo.common.apiclient.interceptor;

/**
 * @author weishao zeng
 * <br/>
 */
public interface ApiInterceptor {

	Object intercept(ApiInterceptorChain chain);

}
