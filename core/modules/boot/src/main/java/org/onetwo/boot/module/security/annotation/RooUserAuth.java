package org.onetwo.boot.module.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.boot.core.web.mvc.annotation.Interceptor;
import org.onetwo.boot.module.security.interceptor.RootUserInterceptor;

/**
 * @author wayshall
 * <br/>
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Interceptor(value=RootUserInterceptor.class)
public @interface RooUserAuth {
}
