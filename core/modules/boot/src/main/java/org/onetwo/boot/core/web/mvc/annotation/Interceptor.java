package org.onetwo.boot.core.web.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.boot.core.web.mvc.annotation.Interceptor.Interceptors;
import org.onetwo.boot.core.web.mvc.interceptor.MvcInterceptor;
import org.onetwo.common.spring.annotation.Property;

/**
 * @author wayshall
 * <br/>
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Repeatable(Interceptors.class)
public @interface Interceptor {

	Class<? extends MvcInterceptor> value();
	
	Property[] properties() default {};
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.METHOD, ElementType.TYPE})
	@Inherited
	public @interface Interceptors {
		
		Interceptor[] value();
	}
}
