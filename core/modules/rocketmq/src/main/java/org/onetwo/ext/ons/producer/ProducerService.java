package org.onetwo.ext.ons.producer;

import org.onetwo.ext.alimq.OnsMessage;
import org.onetwo.ext.alimq.SendMessageErrorHandler;

import com.aliyun.openservices.ons.api.SendResult;

/**
 * @author wayshall
 * <br/>
 */
public interface ProducerService extends TraceableProducer {

	void sendMessage(String topic, String tags, Object body);

	SendResult sendMessage(OnsMessage onsMessage);

	SendResult sendMessage(OnsMessage onsMessage, SendMessageErrorHandler<SendResult> errorHandler);

}