package org.onetwo.ext.ons.consumer;

import java.util.Properties;

import org.onetwo.ext.ons.ListenerType;
import org.onetwo.ext.ons.annotation.ONSSubscribe.IdempotentType;

import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;

import lombok.Builder;
import lombok.Data;

/**
 * maxReconsumeTimes:
 * 一个消息如果消费失败的话，最多重新消费多少次才投递到死信队列	-1
注，这个值默认值虽然是-1，但是实际使用的时候默认并不是-1。按照消费是并行还是串行消费有所不同的默认值。
并行：默认16次
串行：默认无限大（Interge.MAX_VALUE）。由于顺序消费的特性必须等待前面的消息成功消费才能消费后面的，默认无限大即一直不断消费直到消费完成。
 * @author wayshall
 * <br/>
 */
@Data
@Builder
public class ConsumerMeta {
	public static final String CONSUME_TIMESTAMP_KEY = "consumeTimestamp";
	
	private String consumerId;
	private String topic;
	private String subExpression;

	private ListenerType listenerType;
	private Object consumerAction;
	private String consumerBeanName;
	
	@Builder.Default
	private MessageModel messageModel = MessageModel.CLUSTERING;
	@Builder.Default
	private ConsumeFromWhere consumeFromWhere = ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET;
	
	// DefaultMQPushConsumer#consumeTimestamp
	/***
	 * 格式：yyyyMMddhhmmss 
	 */
	private String consumeTimestamp;
	
	@Builder.Default
	private long ignoreOffSetThreshold = -1;
	/***
	 * 
	 */
	@Builder.Default
	private int maxReconsumeTimes = -1;
	private Properties comsumerProperties;

	@Builder.Default
	private boolean autoDeserialize = true;

	@Builder.Default
	private IdempotentType idempotentType = IdempotentType.NONE;
	
	public boolean shouldWithTransational() {
		return getIdempotentType()==IdempotentType.DATABASE;
	}
	
	public Object getConsumerAction() {
		return consumerAction;
	}

}
