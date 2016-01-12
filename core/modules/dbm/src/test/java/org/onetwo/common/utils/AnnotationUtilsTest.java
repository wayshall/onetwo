package org.onetwo.common.utils;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.annotation.AnnotationUtils;
import org.onetwo.common.utils.AnnotationUtilsTest.TestMenu.SubTestMenu1;
import org.onetwo.common.utils.AnnotationUtilsTest.TestMenu.SubTestMenu1.SubTestMenu11;
import org.onetwo.common.utils.AnnotationUtilsTest.TestMenu.SubTestMenu1.SubTestMenu11.SubTestMenu111;
import org.onetwo.common.utils.AnnotationUtilsTest.TestMenu.SubTestMenu2;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;


public class AnnotationUtilsTest {
	
	public static interface TestMenu {
		@Deprecated
		public static interface SubTestMenu1 {
			public static interface SubTestMenu11 {
				public static interface SubTestMenu111 {
				}
			}
		}
		@Deprecated
		public static interface SubTestMenu2 {
		}
		public static interface SubTestMenu3 {
		}
	}
	
	public static class SubTestMenu11Impl implements SubTestMenu2 {
		
	}
	
	@Test
	public void testSuperClass(){
		Class<?> sclass = SubTestMenu11.class.getSuperclass();
		System.out.println("SubTestMenu11 super class: " +sclass);
		Type type = SubTestMenu11.class.getGenericSuperclass();
		System.out.println("SubTestMenu11 super type: " +type);

		Class<?>[] intes = SubTestMenu11.class.getDeclaredClasses();
		for(Class c :intes){
			System.out.println("getDeclaredClasses :" + c);
		}
		Assert.assertTrue(ArrayUtils.contains(intes, SubTestMenu111.class));
		
		Class<?> dclass = SubTestMenu11.class.getDeclaringClass();
		Assert.assertEquals(dclass, SubTestMenu1.class);
		dclass = AnnotationUtilsTest.class.getDeclaringClass();
		Assert.assertEquals(dclass, null);
		
	}

	@Test
	public void testContainsAny(){
		Annotation[] annos = UserEntity.class.getAnnotations();
		boolean rs = AnnotationUtils.containsAny(annos, Entity.class);
		Assert.assertTrue(rs);
		rs = AnnotationUtils.containsAny(annos, ManyToOne.class);
		Assert.assertFalse(rs);
	}

	@Test
	public void testFindAnnotationWith(){
		Deprecated dep = AnnotationUtils.findAnnotationWithSupers(SubTestMenu11.class, Deprecated.class);
		Assert.assertNull(dep);
		
		dep = AnnotationUtils.findAnnotationWithInterfaces(SubTestMenu11Impl.class, Deprecated.class);
		Assert.assertTrue(Deprecated.class.isInstance(dep));
		
		dep = AnnotationUtils.findAnnotationWithDeclaring(SubTestMenu111.class, Deprecated.class);
		Assert.assertTrue(Deprecated.class.isInstance(dep));
	}
}
