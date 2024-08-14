package org.onetwo.ext.ons.producer;

import org.apache.rocketmq.client.producer.SendResult;
import org.onetwo.boot.mq.InterceptableMessageSender;


/**
 * @author weishao zeng
 * <br/>
 */
public interface DefaultProducerService extends RmqProducerService {

	InterceptableMessageSender<SendResult> getInterceptableMessageSender();
	
}

