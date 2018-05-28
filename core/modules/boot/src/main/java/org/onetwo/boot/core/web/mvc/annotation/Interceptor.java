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
 * 规则：
 * 可重复；
 * 首先在方法上查找拦截器配置，如果找到，则停止查找，并返回配置；
 * 如果方法没有，则在类和父类查找，合并返回拦截器配置；
 * 
 * 
 * 如果返回的拦截器配置里有@InterceptorDisabled，则所有拦截器失效
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
	 * 若设置为false，并且properties属性为空，则会从spring的applicationContext里查找，没有找到则创建并根据元数据属性缓存
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
