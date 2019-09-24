package org.onetwo.boot.core.jwt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.boot.core.jwt.JwtMvcInterceptor;
import org.onetwo.boot.core.web.mvc.annotation.Interceptor;
import org.onetwo.common.spring.annotation.Property;

/**
 * 没有token则忽略，有token则解释验证
 * @author wayshall
 * <br/>
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Interceptor(value = JwtMvcInterceptor.class,
	properties = {
		@Property(name = "canBeAnonymous", value = "true")
	}
)
public @interface AnonymousOrJwtUserAuth {
	
	/*public static class AnonymousOrUserAuthInterceptor extends JwtMvcInterceptor {
	}*/
}
