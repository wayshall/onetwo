package org.onetwo.ext.ons.producer;

public interface SendMessageInterceptor {
	
	Object intercept(SendMessageInterceptorChain chain);

}
