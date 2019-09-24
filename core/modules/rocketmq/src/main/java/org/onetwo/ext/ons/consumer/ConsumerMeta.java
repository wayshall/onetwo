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
	final private String consumerId;
	final private String topic;
	final private String subExpression;
	final private MessageModel messageModel;
	final private ConsumeFromWhere consumeFromWhere;
	final private long ignoreOffSetThreshold;
	final private ListenerType listenerType;
	final private Object consumerAction;
	final private String consumerBeanName;
	/***
	 * 
	 */
	final private int maxReconsumeTimes;
	private Properties comsumerProperties;
	
	private boolean autoDeserialize;
	
	private IdempotentType idempotentType;
	
	public boolean shouldWithTransational() {
		return getIdempotentType()==IdempotentType.DATABASE;
	}

}
