package org.onetwo.dbm.interceptor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DbmInterceptorFilter {
	
	InterceptorType type();

	enum InterceptorType {
		SESSION,
		JDBC,
		REPOSITORY
	}
}
