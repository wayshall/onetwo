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
	
	/***
	 * 
	 * @return
	 * @Deprecated 去掉这个参数，不再需要指明是否查询参数
	 */
	@Deprecated
	public boolean queryParam() default true;
	
	/****
	 * 注意，此处设置只对query.setParameter有用，生成的sql仍需自己控制是否生成参数
	 * @Deprecated 去掉这个参数，不再需要
	 * @return
	 */
	@Deprecated
	public IfNull ifParamNull() default IfNull.Calm;
	
	/***
	 * 如果参数是数组和列表，使用name+index重新生成参数名称
	 * cardNo in ( ${_func.inParams('cardNo', cardNos.size())} )
	 * @return
	 */
	public boolean renamedUseIndex() default false;
	
	public boolean isLikeQuery() default false;
	
}
