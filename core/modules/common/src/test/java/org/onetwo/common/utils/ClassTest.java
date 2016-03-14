package org.onetwo.common.utils;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.reflect.ReflectUtils;

public class ClassTest {
	
	public void queryPageByUserName(Page<UserEntity> page, String userName){
		
	}
	
	@Test
	public void testGenericType(){
		Method method = ReflectUtils.findMethod(this.getClass(), "queryPageByUserName", Page.class, String.class);
		System.out.println("method: " + method);
		Page<UserEntity> page = Page.create();
		Assert.assertTrue(Page.class.isInstance(page));
	}

}
