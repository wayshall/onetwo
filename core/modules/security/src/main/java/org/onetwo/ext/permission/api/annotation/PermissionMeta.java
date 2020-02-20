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
	int sort() default Integer.MIN_VALUE;
	
	/****
	 * 权限类型
	 * @author weishao zeng
	 * @return
	 */
	PermissionType permissionType() default PermissionType.MENU;
	
	/***
	 * 是否隐藏
	 * 
	 * 可以通过此属性控制是否出现在菜单栏里
	 * 
	 * @author weishao zeng
	 * @return
	 */
	boolean hidden() default false;
	
	String url() default "";
	
	/****
	 * 前端组件路径
	 * @author weishao zeng
	 * @return
	 */
//	String componentViewPath() default "";
	
	/***
	 * 权限验证拦截的url模式
	 * @author weishao zeng
	 * @return
	 */
	String[] resourcesPattern() default {};
	
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
