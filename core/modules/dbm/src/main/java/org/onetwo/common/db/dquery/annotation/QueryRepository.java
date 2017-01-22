package org.onetwo.common.db.dquery.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryRepository {
	
	/*****
	 * QueryProvideManager beanName
	 * @return
	 */
	String provideManager() default "";
	String dataSource() default "";
	
	
}
