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
 * 配置参考 PropertyKeyConst
 * 
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ONSSubscribe {
	
	/****
	 * 若不指定，则默认使用消费者类的simpleName+"_"+方法名称 作为消费者id
	 * 
	 * @author weishao zeng
	 * @return
	 */
	String consumerId() default "";
	String topic();
	String subExpression() default "";
	String[] tags() default {};
	MessageModel messageModel() default MessageModel.CLUSTERING;
	ConsumeFromWhere consumeFromWhere() default ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET ;

	// DefaultMQPushConsumer#consumeTimestamp
	/***
	 * 格式：yyyyMMddhhmmss 
	 */
	String consumeTimestamp() default "";
	
	long ignoreOffSetThreshold() default -1;
	int maxReconsumeTimes() default -1;
	boolean autoDeserialize() default true;
	IdempotentType idempotent() default IdempotentType.NONE;
	
	//consumeTimeoutInMinutes
	//properties: field=vlaue
	
	ConsumerProperty[] properties() default {};
	
	public enum IdempotentType {
		NONE,
		DATABASE;
	}
	
	@Target({ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ConsumerProperty {
		String name();
		String value();
	}
}
