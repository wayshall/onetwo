package org.onetwo.boot.core.jwt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.boot.core.jwt.JwtMvcInterceptor;
import org.onetwo.boot.core.web.mvc.annotation.Interceptor;

/**
 * @author wayshall
 * <br/>
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Interceptor(value=JwtMvcInterceptor.class)
public @interface JwtUserAuth {
}
