package org.onetwo.common.web.s2.security.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.common.web.s2.security.Authenticator;



@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
//若父类的注解定义在类上面，那么子类可以继承
//若父类的注解定义在方法上面，那么子类可以继承 
//若子类重写了父类中定义了注解的方法，那么子类无法继承该方法的注解 ，即子类在重写父类中被@Inherited标注的方法时，会将该方法连带它上面的注解一并覆盖掉  
//接口的实现类无法继承接口中定义的被@Inherited标注的注解
@Inherited
public @interface Authentic {
	public static final String DEFAULT_REDIRECT = "";//"redirect:/login";
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
	String redirect() default DEFAULT_REDIRECT;
	String[] roles() default {};
}
