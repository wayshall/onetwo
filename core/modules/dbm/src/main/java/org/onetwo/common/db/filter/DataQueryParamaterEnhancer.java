package org.onetwo.common.db.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/****
 * 数据过滤注解
 * 由 {@link org.onetwo.common.db.filter.annotation.DataQueryFilterListener DataQueryFilterListener} 实现过滤
 * @author weishao
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataQueryParamaterEnhancer {
	
	Class<? extends IDataQueryParamterEnhancer> value();

}
