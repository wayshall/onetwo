package org.onetwo.ext.ons;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.onetwo.ext.ons.consumer.CustomONSConsumer;

import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;

/**
 * @author wayshall
 * <br/>
 */
@AllArgsConstructor
public enum ListenerType {
	ONS(MessageListener.class),
	RMQ(MessageListenerConcurrently.class),
	CUSTOM(CustomONSConsumer.class);
	
	@Getter
	final private Class<?> listenerClass;
	
}
