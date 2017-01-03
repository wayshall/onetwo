package org.onetwo.ext.permission.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/****
 * 代理的菜单
 * 可以把独立的菜单类挂载到已有的菜单类的某个节点上
 * 实际效果和children={SubModuleMenu.class}一样
 * <pre>{@code 
 * RootMenu{
 * 		@ProxyMenu(SubModuleMenu.class)
 * 		SecondMenu {
 * 		}
 * 
 * 		ThirdMenu{
 * 			Menu1{
 * 				// generate code: RootMenu_ThirdMenu_Menu1
 * 			}
 * 		}
 * }
 * 
 * SubModuleMenu {
 * 		SubMenu1 {
 * 			// generate code: RootMenu_SecondMenu_Menu1
 * 		}
 * }
 * 
 * }</pre>
 * @author wayshall
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProxyMenu {
	Class<?> value();
}
