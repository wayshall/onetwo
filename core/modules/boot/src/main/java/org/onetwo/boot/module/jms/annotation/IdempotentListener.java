package org.onetwo.boot.module.jms.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.boot.mq.IdempotentType;
import org.springframework.jms.annotation.JmsListener;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IdempotentListener {


	/**
	 * @see JmsListener#id()
	 */
	String id() default "";

	/**
	 * @see JmsListener#containerFactory()
	 */
	String containerFactory() default "";

	/**
	 * @see JmsListener#destination()
	 */
	String destination();

	/**
	 * @see JmsListener#subscription()
	 */
	String subscription() default "";

	/**
	 * @see JmsListener#selector()
	 */
	String selector() default "";

	/***
	 * @see JmsListener#concurrency()
	 */
	String concurrency() default "";
	
	IdempotentType idempotentType() default IdempotentType.NONE;
}
