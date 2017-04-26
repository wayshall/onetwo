package org.onetwo.ext.rocketmq.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.ext.rocketmq.config.RocketMQConfiguration;
import org.springframework.context.annotation.Import;

/**
 * @author wayshall
 * <br/>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(RocketMQConfiguration.class)
public @interface EnableRocketMQ {

}
