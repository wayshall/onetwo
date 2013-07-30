package org.onetwo.plugins.permission.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JResource {
	
	String assemble() default "";	
	String label();
	boolean menu() default false;
	boolean permission() default false;
	Class<?> parentResource() default Object.class;

}
