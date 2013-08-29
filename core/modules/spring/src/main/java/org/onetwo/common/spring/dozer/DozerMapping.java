package org.onetwo.common.spring.dozer;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DozerMapping {

	Class<?>[] classb() default {Object.class};
	boolean mapNull() default false;
	boolean mapEmpty() default true;
	String fieldSplit() default "";
}
