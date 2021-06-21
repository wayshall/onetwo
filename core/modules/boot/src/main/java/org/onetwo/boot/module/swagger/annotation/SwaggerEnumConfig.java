package org.onetwo.boot.module.swagger.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author weishao zeng
 * <br/>
 */

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SwaggerEnumConfig {
	
	/***
	 * 默认使用name()
	 * @author weishao zeng
	 * @return
	 */
	String valueProperty() default "name";
	
	String labelProperty() default "label";

}
