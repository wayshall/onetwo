package org.onetwo.common.dbm;

import java.lang.annotation.Annotation;

import javax.persistence.Id;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.dbm.model.entity.UserEntity;
import org.onetwo.common.reflect.ReflectUtils;


public class IgnoreAnnotationTest {
	 
	@Test
	public void testCopyIgnoreAnnotations(){
		Class<? extends Annotation>[] classes = new Class[]{Id.class};
		UserEntity source = new UserEntity();
		source.setId(11L);
		source.setUserName("test");
		UserEntity target = new UserEntity();
		ReflectUtils.copyIgnoreAnnotations(source, target);
		Assert.assertEquals(source.getId(), target.getId());
		Assert.assertEquals(source.getUserName(), target.getUserName());
		
		target = new UserEntity();
		ReflectUtils.copyIgnoreAnnotations(source, target, classes);
		Assert.assertNull(target.getId());
		Assert.assertEquals(source.getUserName(), target.getUserName());
	}
	
	

}
