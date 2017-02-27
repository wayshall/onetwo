package org.onetwo.dbm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DbmNestedResult {
	String property();
	String idField() default "";
	String columnPrefix() default "";
	NestedType nestedType();
	
	public enum NestedType {
		ASSOCIATION,
		COLLECTION,
		MAP
	}
}
