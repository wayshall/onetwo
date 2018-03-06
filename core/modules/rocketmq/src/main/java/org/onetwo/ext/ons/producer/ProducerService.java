package org.onetwo.ext.ons.producer;

import org.onetwo.boot.mq.SendMessageInterceptor.InterceptorPredicate;
import org.onetwo.ext.alimq.OnsMessage;

import com.aliyun.openservices.ons.api.SendResult;

/**
 * @author wayshall
 * <br/>
 */
public interface ProducerService extends TraceableProducer, org.onetwo.boot.mq.ProducerService {

	void sendMessage(String topic, String tags, Object body);

	SendResult sendMessage(OnsMessage onsMessage);
	SendResult sendMessage(OnsMessage onsMessage, InterceptorPredicate interceptorPredicate);


}