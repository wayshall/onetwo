package org.onetwo.ext.ons.producer;

import com.aliyun.openservices.ons.api.SendResult;

public interface SendMessageInterceptor {
	
	SendResult intercept(SendMessageInterceptorChain chain);

}
