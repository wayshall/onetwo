package org.onetwo.common.ioc.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@SuppressWarnings("rawtypes")
@Target({TYPE, METHOD}) @Retention(RUNTIME)
public @interface BFInterceptors {
	Class[] value();
}
