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
public @interface DUIChildEntity {
	
	Class<?> entityClass();
	/***
	 * 关联父实体id的字段，相当于外键
	 * @author weishao zeng
	 * @return
	 */
	String refParentField();
}
