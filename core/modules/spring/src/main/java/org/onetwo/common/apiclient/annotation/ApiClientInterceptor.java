package org.onetwo.common.apiclient.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.common.apiclient.interceptor.ApiInterceptor;

/**
 * TODO：待实现
 * api拦截器
 * @author wayshall
 * <br/>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ApiClientInterceptor {
	
	Class<? extends ApiInterceptor>[] value();
}
