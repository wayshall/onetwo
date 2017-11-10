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
 * 把注解放在有且只有一个MessageExt类型参数的方法上
 *
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ONSSubscribe {

	String consumerId();
	String topic();
	String subExpression() default "*";
	MessageModel messageModel() default MessageModel.CLUSTERING;
	ConsumeFromWhere consumeFromWhere() default ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET ;
	long ignoreOffSetThreshold() default -1;
	int maxReconsumeTimes() default 16;
}
