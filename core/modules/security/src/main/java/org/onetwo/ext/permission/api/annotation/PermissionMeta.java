package org.onetwo.ext.permission.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.ext.permission.api.PermissionType;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionMeta {
	/***
	 * 排序
	 * @author weishao zeng
	 * @return
	 */
	int sort() default 0;
	
	/****
	 * 权限类型
	 * @author weishao zeng
	 * @return
	 */
	PermissionType permissionType() default PermissionType.MENU;
	
	boolean hidden() default false;
	
	String url() default "";
	
	String resourcesPattern() default "";
	
	String name() default "";
	
	Class<?>[] children() default {};
	
	/***
	 * json data
	 * @author weishao zeng
	 * @return
	 */
	String meta() default "";
	String params() default "";
	
	/*String cssClass() default "";
	
	String showProps() default "";*/
}
