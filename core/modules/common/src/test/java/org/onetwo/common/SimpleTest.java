package org.onetwo.common;

import org.junit.Test;
import org.onetwo.common.utils.UserEntity;

public class SimpleTest {
	
	@Test
	public void test(){
		String str = "test";
		System.out.println("hc:"+str.hashCode());
		System.out.println("hc:"+Integer.valueOf(222222222).hashCode());
		UserEntity u = new UserEntity();
		u.setUserName("test");
		System.out.println("u hc:"+u.hashCode());
		

		System.out.println("Integer:"+Integer.valueOf(20002).hashCode());
		System.out.println("Long:"+Long.valueOf(20002).hashCode());
		
	}

}
