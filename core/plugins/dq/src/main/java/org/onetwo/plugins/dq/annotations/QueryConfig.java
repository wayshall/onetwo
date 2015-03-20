package org.onetwo.plugins.dq.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.common.db.QueryContextVariable;
import org.onetwo.common.spring.sql.ParserContextFunctionSet;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryConfig {

	/***
	 * 配置参数中模糊查询的字段
	 * @return
	 */
	public String[] likeQueryFields() default {};
	public boolean stateful() default true;
	public Class<? extends QueryContextVariable> funcClass() default ParserContextFunctionSet.class;
	
	
}
