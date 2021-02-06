package org.onetwo.boot.module.activemq.mqtt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.boot.module.activemq.mqtt.ActiveMQTTProperties.MessageConverters;

/**
 * @author wayshall
 * <br/>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MqttInboundHandler {
	
	String clientId();
	
	String channelName() default "";
	
	String[] topics() default {};
	
	int[] qos() default { 2 };
	
	int completionTimeout() default 30000;
	
	MessageConverters converter() default MessageConverters.DEFAULT;
	
}
