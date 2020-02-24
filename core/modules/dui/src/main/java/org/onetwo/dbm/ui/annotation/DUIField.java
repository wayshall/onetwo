package org.onetwo.dbm.ui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author weishao zeng
 * <br/>
 */

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DUIField {
	
	/****
	 * 显示的label
	 * @author weishao zeng
	 * @return
	 */
	String label();
	String listField() default "";
	
	boolean searchable() default false;
	boolean listable() default true;
	boolean insertable() default true;
	boolean updatable() default true;
	int order() default 100;
}
