package org.onetwo.ext.ons;

import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.onetwo.ext.ons.consumer.CustomONSConsumer;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wayshall
 * <br/>
 */
@AllArgsConstructor
public enum ListenerType {
//	ONS(MessageListener.class),
	RMQ(MessageListenerConcurrently.class),
	CUSTOM(CustomONSConsumer.class);
	
	@Getter
	final private Class<?> listenerClass;
	
}
