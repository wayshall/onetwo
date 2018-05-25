package org.onetwo.boot.core.web.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.boot.core.web.mvc.annotation.InterceptorDisabled.DisableMvcInterceptor;
import org.onetwo.boot.core.web.mvc.interceptor.MvcInterceptorAdapter;

/**
 * @author wayshall
 * <br/>
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Interceptor(DisableMvcInterceptor.class)
@Inherited
public @interface InterceptorDisabled {

	public final class DisableMvcInterceptor extends MvcInterceptorAdapter {
		private DisableMvcInterceptor(){
		}
	}
}
