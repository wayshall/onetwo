package org.onetwo.dbm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DbmNestedResult {
	String property();
	/***
	 * 指定类的某个属性作为唯一键
	 * @return
	 */
	String id() default "";
	String columnPrefix() default "";
	NestedType nestedType();
	
	public enum NestedType {
		ASSOCIATION,
		COLLECTION,
		MAP
	}
}
