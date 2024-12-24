package org.onetwo.boot.module.poi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.ext.poi.excel.generator.TemplateRowTypes;

/**
 * 对应 org.onetwo.ext.poi.excel.generator.RowModel
 * @author weishao zeng
 * <br/>
 */

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelRow {
	/***
	 * 默认不设置
	 * @return
	 */
	short height() default -1;
	String fieldStyle() default "";
	String fieldFont() default "";
	String name() default "";
	TemplateRowTypes type() default TemplateRowTypes.ROW;
	
	ExcelField[] fields();
}
