package org.onetwo.common.db.dquery.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/****
 * 标注了此注解的方法，会在执行的时候把对应的sql，转换为count统计函数的sql
 * @author way
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AsCountQuery {
	/***
	 * query name
	 * @return
	 */
	public String value();
}
