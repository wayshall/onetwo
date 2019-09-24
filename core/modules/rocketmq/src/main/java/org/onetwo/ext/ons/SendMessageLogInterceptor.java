package org.onetwo.ext.ons;

import org.onetwo.boot.mq.interceptor.SendMessageInterceptor;
import org.onetwo.ext.ons.producer.ONSSendMessageContext;
import org.slf4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;

/**
 * 应该放在最后一个拦截
 * @author wayshall
 * <br/>
 */
@Order(Ordered.LOWEST_PRECEDENCE)
public class SendMessageLogInterceptor implements SendMessageInterceptor {
	private final Logger logger = ONSUtils.getONSLogger();

	
	public SendMessageLogInterceptor() {
	}

	@Override
	public SendResult intercept(org.onetwo.boot.mq.interceptor.SendMessageInterceptorChain chain) {
		ONSSendMessageContext ctx = (ONSSendMessageContext)chain.getSendMessageContext();
		Message message = ctx.getMessage();
		if(logger.isInfoEnabled()){
			logger.info("send message topic: {}, tags: {}, key: {}", message.getTopic(), message.getTag(), message.getKey());
		}
		SendResult result = (SendResult)chain.invoke();
		if(logger.isInfoEnabled()){
			logger.info("send message success. topic: {}, tags: {}, sendResult: {}", message.getTopic(), message.getTag(), result);
		}
		return result;
	}

}
