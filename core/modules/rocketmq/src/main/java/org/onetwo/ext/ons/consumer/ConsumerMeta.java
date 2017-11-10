package org.onetwo.ext.ons.consumer;

import lombok.Builder;
import lombok.Value;

import org.onetwo.ext.ons.ListenerType;

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
	final private String listenerName;
	final private int maxReconsumeTimes;

}
