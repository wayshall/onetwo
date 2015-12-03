package org.onetwo.common.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Type;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.reflect.Intro;
import org.onetwo.common.reflect.ReflectUtils;

public class ClassWrapperTest {
	
	@Test
	public void testNewInstanceFromMap(){
		Intro<UserEntity> jcl = new Intro(UserEntity.class);
		UserEntity bean = jcl.newFrom(LangUtils.asMap("userName", "way", "age", 11));
		LangUtils.println("user : ${0}", bean);
	}
	

	@Test
	public void testGetGenericType(){
		PropertyDescriptor pd = ReflectUtils.findProperty(UserEntity.class, "roles");
		System.out.println("type: " + pd.getPropertyType());
		
		Intro cw = Intro.wrap(UserEntity.class);
		JFishPropertyInfoImpl jp = new JFishPropertyInfoImpl(cw, pd);
		Type type = jp.getParameterTypes()[0];
		Assert.assertEquals(RoleEntity.class, type);
		
	}

}
