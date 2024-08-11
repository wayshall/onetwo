package org.onetwo.ext.ons.producer;

import org.apache.rocketmq.client.producer.SendResult;
import org.onetwo.ext.alimq.OnsMessage;

/**
 * @author wayshall
 * <br/>
 */
public interface ProducerService extends TraceableProducer, org.onetwo.boot.mq.ProducerService<OnsMessage, SendResult> {

	void sendMessage(String topic, String tags, Object body);
	void send(OnsMessage onsMessage);

	/*SendResult sendMessage(OnsMessage onsMessage);
	SendResult sendMessage(OnsMessage onsMessage, InterceptorPredicate interceptorPredicate);*/


}