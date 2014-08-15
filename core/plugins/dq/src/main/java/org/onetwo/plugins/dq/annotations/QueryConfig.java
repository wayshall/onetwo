package org.onetwo.plugins.dq.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryConfig {

	/***
	 * 配置参数中模糊查询的字段
	 * @return
	 */
	public String[] likeQueryFields() default {};
	
	
}
