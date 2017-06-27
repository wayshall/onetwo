package org.onetwo.common.spring.condition;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Conditional;

/**
 * @author wayshall
 * <br/>
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnBeanCondition.class)
public @interface OnExistingBean {
	
	/***
	 * or 
	 * 查找所有指定类型的bean，结果不为空即为匹配成功
	 * @author wayshall
	 * @return
	 */
	Class<?>[] value() default {};
	
}
