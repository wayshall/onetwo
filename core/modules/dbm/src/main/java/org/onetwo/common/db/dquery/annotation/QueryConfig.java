package org.onetwo.common.db.dquery.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.common.db.QueryContextVariable;
import org.onetwo.common.db.filequery.ParserContextFunctionSet;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryConfig {

	/***
	 * 配置参数中模糊查询的字段
	 * @return
	 */
	public String[] likeQueryFields() default {};
	/*** 
	 * 如果是hibernate实现，该方法决定使用何种session
	 * @return
	 */
	public boolean stateful() default true;
	public Class<? extends QueryContextVariable> funcClass() default ParserContextFunctionSet.class;
	
	
}
