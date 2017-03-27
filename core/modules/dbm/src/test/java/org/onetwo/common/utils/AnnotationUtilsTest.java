package org.onetwo.common.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.annotation.AnnotationUtils;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.AnnotationUtilsTest.TestMenu.SubTestMenu1;
import org.onetwo.common.utils.AnnotationUtilsTest.TestMenu.SubTestMenu1.SubTestMenu11;
import org.onetwo.common.utils.AnnotationUtilsTest.TestMenu.SubTestMenu1.SubTestMenu11.SubTestMenu111;
import org.onetwo.common.utils.AnnotationUtilsTest.TestMenu.SubTestMenu2;
import org.onetwo.common.utils.AnnotationUtilsTest.TestMenu.SubTestMenu3;
import org.springframework.transaction.annotation.Transactional;


public class AnnotationUtilsTest {
	
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
		Annotation[] annos = UserEntityForAnnotationTest.class.getAnnotations();
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

	@Test
	public void testFindMethodAnnotation(){
		Method method = ReflectUtils.findMethod(SubTestMenu3.class, "menu");
		Transactional transactional = AnnotationUtils.findAnnotationWithStopClass(SubTestMenu11Impl.class, method, Transactional.class);
		assertThat(transactional).isNotNull();
	}
	
	public static interface TestMenu {
		public static interface TestMenuEmptyInterface {
		}
		@Deprecated
		public static interface SubTestMenu1 {
			public static interface SubTestMenu11 {
				public static interface SubTestMenu111 {
				}
			}
		}
		@Deprecated
		public static interface SubTestMenu2 extends TestMenuEmptyInterface{
			@Transactional
			void menu();
		}
		public static interface SubTestMenu3 {
			void menu();
		}
	}
	
	public static class SubTestMenu11Impl implements SubTestMenu2 {
		public void menu(){
			
		}
		
	}
	
	@Entity
	static public class UserEntityForAnnotationTest {

		private Long id;
		
		private String userName;

		private String status;
		
		private String email;
		
		private Integer age;

		private Date birthDay;

		private Float height;
		
		private List<RoleEntity> roles;
		
		private Map<String, Object> attrs;
		
		private int[] bust;
		
		public Long getId() {
			return id;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public Integer getAge() {
			return age==null?0:age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}

		public Date getBirthDay() {
			return birthDay;
		}

		public void setBirthDay(Date birthDay) {
			this.birthDay = birthDay;
		}

		public Float getHeight() {
			return height==null?0:height;
		}

		public void setHeight(Float height) {
			this.height = height;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
		
		public void addRole(RoleEntity role){
			if(this.roles==null)
				this.roles = new ArrayList<RoleEntity>();
			this.roles.add(role);
		}

		public List<RoleEntity> getRoles() {
			return roles;
		}

		public void setRoles(List<RoleEntity> roles) {
			this.roles = roles;
		}

		public Map<String, Object> getAttrs() {
			return attrs;
		}

		public void setAttrs(Map<String, Object> attrs) {
			this.attrs = attrs;
		}

		public int[] getBust() {
			return bust;
		}

		public void setBust(int[] bust) {
			this.bust = bust;
		}


	}
}
