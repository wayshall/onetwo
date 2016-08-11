package org.onetwo.common.db.dquery.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {

	public String value();
	
	/***
	 * 如果参数是数组和列表，使用name+index重新生成参数名称
	 * cardNo in ( ${_func.inParams('cardNo', cardNos.size())} )
	 * @return
	 */
	public boolean renamedUseIndex() default false;
	
	public boolean isLikeQuery() default false;
	
}
