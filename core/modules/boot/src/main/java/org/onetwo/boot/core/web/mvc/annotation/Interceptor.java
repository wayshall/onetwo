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
	
	/***
	 * 是否总是创建新实例
	 * 若设置为false，则会优先从spring的applicationContext里查找，没有找到则创建并根据注解类型和属性缓存
	 * @author wayshall
	 * @return
	 */
	boolean alwaysCreate() default false;
	
	Property[] properties() default {};
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.METHOD, ElementType.TYPE})
	@Inherited
	public @interface Interceptors {
		
		Interceptor[] value();
	}
}
