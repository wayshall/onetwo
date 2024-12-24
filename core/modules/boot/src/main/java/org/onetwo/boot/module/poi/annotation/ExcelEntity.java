package org.onetwo.boot.module.poi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.ext.poi.excel.generator.PoiModel;

/**
 * 标记是否可导出
 * @author weishao zeng
 * <br/>
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelEntity {
	String name() default "";
	String format() default PoiModel.FORMAT_XLSX;
	boolean autoSizeColumn() default false;
	ExcelRow[] rows() default {};
}
