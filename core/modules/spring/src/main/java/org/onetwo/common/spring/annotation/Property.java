package org.onetwo.common.spring.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author wayshall
 * <br/>
 */
//@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {
//	Class<?> targetClass() default Object.class;
	String name();
	String value();
//	Class<?> type();

}
