package org.example;

import org.example.utils.MenuInfo;
import org.example.utils.MenuInfo.SystemManager;
import org.example.utils.MenuInfo.SystemManager.UserManager;
import org.junit.Test;
import org.onetwo.common.utils.ReflectUtils;

public class MenuTest {
	
	@Test
	public void testMenuInfo(){
		Class<?>[] clses = MenuInfo.class.getDeclaredClasses();
		Class<?> parent = MenuInfo.class.getDeclaringClass();
		System.out.println("menu parent: " + parent);
		for(Class<?> cls : clses){
			System.out.println("menu cls: " +cls);
		}
		
		clses = UserManager.class.getDeclaredClasses();
		System.out.println(clses.length);
		for(Class<?> cls : clses){
			System.out.println("user cls: " +cls);
		}
		parent = UserManager.class.getDeclaringClass();
		System.out.println("user parent: " + parent);
		System.out.println("menu final: " + ReflectUtils.getFinalDeclaringClass(MenuInfo.class));
		System.out.println("sys final: " + ReflectUtils.getFinalDeclaringClass(SystemManager.class));
		System.out.println("user final: " + ReflectUtils.getFinalDeclaringClass(UserManager.class));
	}

}
