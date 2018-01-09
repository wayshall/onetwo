package org.onetwo.ext.ons.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.ext.ons.ONSConfiguration;
import org.onetwo.ext.ons.producer.ProducerRegistar;
import org.springframework.context.annotation.Import;

/**
 * @author wayshall
 * <br/>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({ONSConfiguration.class, ProducerRegistar.class})
public @interface EnableONSClient {
//	String[] producerIds() default {};
	ONSProducer[] producers() default {};
}
