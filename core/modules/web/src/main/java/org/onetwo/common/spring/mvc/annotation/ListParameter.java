package org.onetwo.common.spring.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ListParameter {

	/****
	 * the param name prefix witch will bind to list element 
	 * @return
	 */
	public String value() default "";
	
	@SuppressWarnings("rawtypes")
	public Class<? extends List> type() default ArrayList.class; 
	
}
