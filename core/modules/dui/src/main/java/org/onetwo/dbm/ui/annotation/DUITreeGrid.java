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
public @interface DUITreeGrid {
	
	String parentField() default "";
	String rootId() default "0";
}
