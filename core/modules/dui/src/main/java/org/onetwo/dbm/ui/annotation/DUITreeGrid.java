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
	
	TreeStyles style() default TreeStyles.CHIDREN_ON_RIGHT;
	
	public enum TreeStyles {
		/***
		 * 右边显示当前节点的子节点列表
		 */
		CHIDREN_ON_RIGHT,
		/***
		 * 右边显示当前节点的级联数据列表
		 */
		CASCADE_ON_RIGHT,
		
	}
	
}
