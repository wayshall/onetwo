package org.onetwo.plugins.permission.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ByMenu {

	Class<?> id() default Object.class;
	String name() default "";
	String url() default "";
	Class<?> parent() default Object.class;
	boolean pageElement() default false;
}
