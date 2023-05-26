package org.onetwo.boot.func.submit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.boot.core.web.mvc.annotation.Interceptor;
import org.onetwo.boot.core.web.mvc.interceptor.MvcInterceptorManager;
import org.onetwo.boot.func.submit.RedisRateLimiterInterceptor;

/**
 * @see MvcInterceptorManager#findInterceptorAnnotationAttrsList
 * @author wayshall
 * <br/>
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Interceptor(RedisRateLimiterInterceptor.class)
@Inherited
public @interface RateLimiterInterceptor {
}
