package org.onetwo.common.spring.web.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.common.web.asyn.AsyncUtils;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AsyncProcessor {

//	Class<?> messageTunnel() default StringMessageTunnel.class;
	boolean progressProcessor() default false;
	int flushInSecond() default 1;
	String contentType() default AsyncUtils.CONTENT_TYPE;
	
}
