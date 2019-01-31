package org.onetwo.cloud.sleuth;

import org.onetwo.boot.mq.interceptor.SendMessageInterceptor;
import org.onetwo.boot.mq.interceptor.SendMessageInterceptorChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;

/**
 * @author weishao zeng
 * <br/>
 */
public class SleuthRocketmqInterceptor implements SendMessageInterceptor {
	public static final String SPAN_NAME = "inner-rmq-send";
	@Autowired(required=false)
	private Tracer tracer;

	@Override
	public Object intercept(SendMessageInterceptorChain chain) {
		if (tracer!=null) {
			return chain.invoke();
		}
		final Span span = tracer.createSpan(SPAN_NAME);
		try {
			span.tag(Span.SPAN_LOCAL_COMPONENT_TAG_NAME, SPAN_NAME);
			span.logEvent(Span.CLIENT_SEND);
			return chain.invoke();
		} finally{
			span.logEvent(Span.CLIENT_RECV);
			tracer.close(span);
		}
	}
	
	

}

