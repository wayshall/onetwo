package org.onetwo.cloud.sleuth;

import org.onetwo.boot.mq.interceptor.SendMessageInterceptor;
import org.onetwo.boot.mq.interceptor.SendMessageInterceptorChain;

/**
 * @author weishao zeng
 * <br/>
 */
public class SleuthRocketmqInterceptor implements SendMessageInterceptor {
	public static final String SPAN_NAME = "inner-rmq-send";
//	private Tracer tracer;

	@Override
	public Object intercept(SendMessageInterceptorChain chain) {
		//TODO
		return chain.invoke();
	}
	
	

}

