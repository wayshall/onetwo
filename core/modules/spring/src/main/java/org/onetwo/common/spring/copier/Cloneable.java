package org.onetwo.common.spring.copier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cloneable {
	
	boolean keyCloneable() default true;
	/***
	 * 如果集合容器
	 * @return
	 */
	boolean valueCloneable() default true;
}
