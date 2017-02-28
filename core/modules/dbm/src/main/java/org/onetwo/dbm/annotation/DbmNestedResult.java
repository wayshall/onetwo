package org.onetwo.dbm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DbmNestedResult {
	/***
	 * 指定嵌套映射的属性
	 * @return
	 */
	String property();
	/***
	 * 指定类的某个属性作为唯一键
	 * @return
	 */
	String id() default "";
	/***
	 * 默认使用"${property}_"，如果property是嵌套的，则"."会转为"_"
	 * @return
	 */
	String columnPrefix() default "";
	NestedType nestedType();
	
	public enum NestedType {
		ASSOCIATION,
		COLLECTION,
		MAP
	}
}
