package org.onetwo.boot.module.oauth2.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.boot.core.web.mvc.annotation.Interceptor;
import org.onetwo.boot.module.oauth2.clientdetails.ClientDetailsMvcInterceptor;

/**
 * 验证租户token
 * @author wayshall
 * <br/>
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Interceptor(value=ClientDetailsMvcInterceptor.class)
public @interface OAuth2ClientAuth {
}
