package org.onetwo.plugins.dq.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.QueryProvideManager;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryProvider {

	public String value() default "";
	public Class<? extends QueryProvideManager> beanClass() default BaseEntityManager.class;
	
	
}
