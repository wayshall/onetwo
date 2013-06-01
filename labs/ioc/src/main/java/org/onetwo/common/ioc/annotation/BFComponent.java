package org.onetwo.common.ioc.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@SuppressWarnings("rawtypes")
@Target({TYPE}) @Retention(RUNTIME)
public @interface BFComponent {
	String name() default "";
	Class[] businessInterfaces() default Object.class;
	Class[] interceptors() default Object.class;
}
