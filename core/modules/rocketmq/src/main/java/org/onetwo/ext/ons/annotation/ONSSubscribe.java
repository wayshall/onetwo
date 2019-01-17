package org.onetwo.ext.ons.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;


/****
 * @author wayshall
 * 
 * 消费方法最多两个参数，第一个参数为ConsumContext，第二个参数为反序列化后的body
 *
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ONSSubscribe {

	String consumerId();
	String topic();
	String subExpression() default "";
	String[] tags() default {};
	MessageModel messageModel() default MessageModel.CLUSTERING;
	ConsumeFromWhere consumeFromWhere() default ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET ;
	long ignoreOffSetThreshold() default -1;
	int maxReconsumeTimes() default -1;
	boolean autoDeserialize() default true;
	IdempotentType idempotent() default IdempotentType.NONE;
	
	//consumeTimeoutInMinutes
	//properties: field=vlaue
	
	public enum IdempotentType {
		NONE,
		DATABASE;
	}
}
