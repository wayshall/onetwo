package org.onetwo.common.web.s2.security.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.common.web.s2.security.Authenticator;



@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Authentic {
	/*@Deprecated
	String resourceEntity() default "";
	@Deprecated
	String operator() default "";
	@Deprecated
	String resourceId() default "";
	@Deprecated
	String entityId()  default "";
	@Deprecated
	boolean isGetRights() default false;*/
	String[] permissions() default {};	

	Class<? extends Authenticator>[] authenticatorClass() default {};
	String[] authenticator() default {};
	boolean isOnlyAuthenticator() default false;
	boolean ignore() default false;
	
	boolean checkTimeout() default true;
	boolean checkLogin() default true;
	boolean throwIfTimeout() default true;

	String authenticationName() default "";
	String redirect() default "redirect:login";
	String[] roles() default {};
}
