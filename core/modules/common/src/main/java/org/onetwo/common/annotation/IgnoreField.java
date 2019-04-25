package org.onetwo.common.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/****
 * bean转为map时，标记了此注解的字段忽略
 * 如果注解在接口，则不起作用？
 * @author way
 *
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RUNTIME)
@Inherited
public @interface IgnoreField {
	
}
