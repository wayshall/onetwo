package org.onetwo.boot.module.poi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对应 org.onetwo.ext.poi.excel.generator.FieldModel
 * @author weishao zeng
 * <br/>
 */

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelField {
	String value();
	String name() default "";
	String label() default "";
	String colspan() default "";
}
