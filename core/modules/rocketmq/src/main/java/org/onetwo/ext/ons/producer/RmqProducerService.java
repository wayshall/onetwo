package org.onetwo.ext.ons.producer;

import org.apache.rocketmq.client.producer.SendResult;
import org.onetwo.ext.alimq.OnsMessage;

/**
 * @author wayshall
 * <br/>
 */
public interface RmqProducerService extends org.onetwo.boot.mq.ProducerService<OnsMessage, SendResult> {

	/***
	 * 无返回值，自动检查见过是否发送成功，否则抛错
	 * @param topic
	 * @param tags
	 * @param body
	 */
	void sendMessage(String topic, String tags, Object body);
//	void send(OnsMessage onsMessage);

	/*SendResult sendMessage(OnsMessage onsMessage);
	SendResult sendMessage(OnsMessage onsMessage, InterceptorPredicate interceptorPredicate);*/


}