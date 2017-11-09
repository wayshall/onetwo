package org.onetwo.ext.ons.producer;

import org.onetwo.ext.alimq.SendMessageErrorHandler;
import org.onetwo.ext.alimq.SimpleMessage;

import com.aliyun.openservices.ons.api.SendResult;

/**
 * @author wayshall
 * <br/>
 */
public interface ProducerService {

	void sendMessage(String topic, String tags, Object body);

	SendResult sendMessage(SimpleMessage onsMessage);

	SendResult sendMessage(SimpleMessage onsMessage, SendMessageErrorHandler<SendResult> errorHandler);

}