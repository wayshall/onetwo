package org.onetwo.ext.ons.producer;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;

/**
 * @author wayshall
 * <br/>
 */
public interface ProducerService extends TraceableProducer, org.onetwo.boot.mq.ProducerService<Message, SendResult> {

	void sendMessage(String topic, String tags, Object body);

	/*SendResult sendMessage(OnsMessage onsMessage);
	SendResult sendMessage(OnsMessage onsMessage, InterceptorPredicate interceptorPredicate);*/


}