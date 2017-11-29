package org.onetwo.ext.rocketmq.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;

/****
 * @author wayshall
 * 
 * 把注解放在有且只有一个MessageExt类型参数的方法上
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RMQSubscribe {

	String groupName();
	String topic();
	String[] tags() default {};
	MessageModel messageModel() default MessageModel.CLUSTERING;
	ConsumeFromWhere consumeFromWhere() default ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET ;
	long ignoreOffSetThreshold() default -1;
	
	//TODO 可增加回调类完全定制配置
	//configClass() default Object.class;
}
