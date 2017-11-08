package org.onetwo.ext.ons.consumer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;

/**
 * @author wayshall
 * <br/>
 */
@Value
@Builder
public class ConsumerMeta {
	final private String consumerId;
	final private String topic;
	final private String subExpression;
	final private MessageModel messageModel;
	final private ConsumeFromWhere consumeFromWhere;
	final private long ignoreOffSetThreshold;
	final private ListenerType listenerType;
	final private Object listener;
	final private String listenerBeanName;

	@AllArgsConstructor
	public static enum ListenerType {
		ONS(MessageListener.class),
		RMQ(MessageListenerConcurrently.class),
		CUSTOM(CustomConsumer.class);
		
		@Getter
		final private Class<?> listenerClass;
		
	}

}
