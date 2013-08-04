package org.example;

import org.example.utils.MenuInfo;
import org.example.utils.MenuInfo.SystemMenu.UserManager;
import org.junit.Test;

public class MenuTest {
	
	@Test
	public void testMenuInfo(){
		Class<?>[] clses = MenuInfo.class.getDeclaredClasses();
		for(Class<?> cls : clses){
			System.out.println("cls: " +cls);
		}
		
		clses = UserManager.New.class.getDeclaredClasses();
		System.out.println(clses.length);
		for(Class<?> cls : clses){
			System.out.println("cls: " +cls);
		}
	}

}
