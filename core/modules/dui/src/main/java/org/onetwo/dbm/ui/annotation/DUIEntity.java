package org.onetwo.dbm.ui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author weishao zeng
 * <br/>
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DUIEntity {
	
	String name() default "";
	String label();
	
//	UIStype style() default UIStype.GRID;
	
	boolean listPage() default true;
	boolean editPage() default true;
	
	/****
	 * 级联编辑
	 * @author weishao zeng
	 * @return
	 */
	DUICascadeEditable[] cascadeEditableEntities() default {};
}
