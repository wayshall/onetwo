package org.onetwo.ext.permission.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/****
 * 代理的菜单
 * 可以把独立的菜单类挂载到已有的菜单类的某个节点上
 * RootMenu{
 * 		{@code @ProxyMenu}(SubModuleMenu.class)
 * 		ThirdMenu{
 * 			Menu1{
 * 				//generate code: RootMenu_ThirdMenu_Menu1
 * 			}
 * 		}
 * }
 * 
 * SubModuleMenu {
 * }
 * 
 * @author wayshall
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProxyMenu {
	Class<?> value();
}
