package org.onetwo.plugins.dq.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.common.db.ExtQuery.K.IfNull;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Name {

	public String value();
	
	public boolean queryParam() default true;
	
	public IfNull ifParamNull() default IfNull.Calm;
	
	/***
	 * 在queryParam=true前提下，如果参数是数组和列表，使用name+index重新生成参数名称
	 * @return
	 */
	public boolean renamedUseIndex() default false;
	
}
