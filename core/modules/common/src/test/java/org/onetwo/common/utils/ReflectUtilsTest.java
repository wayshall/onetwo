package org.onetwo.common.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.date.NiceDate;
import org.onetwo.common.profiling.TimeProfileStack;
import org.onetwo.common.reflect.CopyConfig;
import org.onetwo.common.reflect.ReflectUtils;

public class ReflectUtilsTest {
	
	@Test
	public void testSuperClass(){
		Class<?> sclass = UserEntity.class.getSuperclass();
		System.out.println("super class: " +sclass);
		Type type = UserEntity.class.getGenericSuperclass();
		System.out.println("super type: " +type);
		System.out.println("getCanonicalName: " +long.class.getCanonicalName());
		
		assertThat(ReflectUtils.findMethod(UserEntity.class, "getId").getName()).isEqualTo("getId");
		Method method = ReflectUtils.findMethod(UserEntity.class, "setId");
		assertThat(method.getReturnType()).isEqualTo(void.class);

		Class<?>[] intes = UserEntity.class.getInterfaces();
		for(Class c :intes){
			System.out.println("c:" + c);
		}
		Type[] types = UserEntity.class.getGenericInterfaces();
		for(Type c : types){
			System.out.println("types:" + c);
		}
		
		sclass = BaseEntity.class.getSuperclass();
		System.out.println("BaseEntity super class: " +sclass);
		type = BaseEntity.class.getGenericSuperclass();
		System.out.println("BaseEntity super type: " +type);
		
		intes = BaseEntity.class.getInterfaces();
		for(Class c :intes){
			System.out.println("BaseEntity getInterfaces:" + c);
		}
		types = BaseEntity.class.getGenericInterfaces();
		for(Type c : types){
			System.out.println("BaseEntity getGenericInterfaces:" + c);
		}
		

		sclass = IBaseEntity.class.getSuperclass();
		System.out.println("IBaseEntity super class: " +sclass);
		type = IBaseEntity.class.getGenericSuperclass();
		System.out.println("IBaseEntity super type: " +type);
		
		intes = IBaseEntity.class.getInterfaces();
		for(Class c :intes){
			System.out.println("IBaseEntity getInterfaces:" + c);
		}
		types = IBaseEntity.class.getGenericInterfaces();
		for(Type c : types){
			System.out.println("IBaseEntity getGenericInterfaces:" + c);
		}
		intes = Serializable.class.getInterfaces();
		Assert.assertTrue(intes.length==0);
		types = Serializable.class.getGenericInterfaces();
		Assert.assertTrue(types.length==0);
		
		PropertyDescriptor prop = ReflectUtils.getIntro(UserEntity.class).getProperty("id");
		Class<?> targetClass = prop.getWriteMethod().getDeclaringClass();
		System.out.println("testSuperClass target: " + targetClass);
		Assert.assertEquals(UserEntity.class, targetClass);
	}

	@Test
	public void testSimple(){
		Method[] methods = User.class.getDeclaredMethods();
		for(Method m : methods){
			System.out.println("m : " + m.getName());
			int index = m.getName().indexOf("a");
			if(index!=-1){
				String[] pnames = StringUtils.split(m.getName().substring(index), "a");
				LangUtils.println("${0}--${1}", index, pnames);
			}
		}
		
	}

	
	@Test
	public void testToMap(){
		User user = new User();
		user.setUserName("testName");
		user.setAge(11);
		user.setBirthDate(NiceDate.New("2013-4-18 14:00:00").getTime());
		Map<String, Object> map = ReflectUtils.toMap(user);
		Assert.assertEquals(false, map.get("avaiable"));
		Assert.assertEquals(11, map.get("age"));
		Assert.assertEquals(map.get("userName"), "testName");
	}
	
	@Test
	public void testToStringMap(){
		User user = new User();
		user.setUserName("testName");
		user.setAge(11);
		user.setBirthDate(NiceDate.New("2013-4-18 14:00:00").getTime());
		Map<String, String> map = ReflectUtils.toStringMap(true, user);
		Assert.assertEquals(map.get("avaiable"), "false");
		Assert.assertEquals(map.get("age"), "11");
		Assert.assertEquals(map.get("userName"), "testName");
	}
	
	@Test
	public void testSetExp(){
		User user = new User();
		user.setUserName("testName");
		user.setAge(11);
		user.setBirthDate(new Date());
		
		Object value = "testName";
		ReflectUtils.setExpr(user, "address.detail", value);
		Assert.assertEquals(user.getAddress().getDetail(), value);
		
//		System.out.println(user.getAddress().getDetail());
	}
	
	@Test
	public void testSetExp2(){
		User user = new User();
		user.setUserName("testName");
		user.setAge(11);
		user.setBirthDate(new Date());
		
		Object value = "true";
		ReflectUtils.setExpr(user, "avaiable", value);
		Assert.assertEquals(user.isAvaiable(), true);
		
//		System.out.println(user.getAddress().getDetail());
	}
	
	@Test
	public void testGetExp(){
		User user = new User();
		user.setUserName("testName");
		user.setAge(11);
		user.setBirthDate(new Date());
		
		User.Address addr = new User.Address();
		addr.setDetail("testDetail");
		addr.setPhone("888888");
		user.setAddress(addr);
		
		Object value = ReflectUtils.getExpr(user, "getAddress().detail");
		Assert.assertEquals(user.getAddress().getDetail(), value);
		
		value = ReflectUtils.getExpr(user, "getAddress2().detail");
		Assert.assertEquals(user.getAddress().getDetail(), value);
	}
	
//	@Test
	public void testGetOgnl() {
		User user = new User();
		user.setUserName("testName");
		user.setAge(11);
		user.setBirthDate(new Date());
		
		User.Address addr = new User.Address();
		addr.setDetail("testDetail");
		addr.setPhone("888888");
		user.setAddress(addr);
		
		Object value = MyUtils.getValue("getAddress().detail", user);
		Assert.assertEquals(user.getAddress().getDetail(), value);
		
		value = ReflectUtils.getExpr(user, "getAddress2.detail");
		Assert.assertEquals(user.getAddress().getDetail(), value);
	}
	

//	@Test
	public void testPerform2(){
		TimeProfileStack.setActive(true);
		int count = 4;
		for(int i=0; i<count; i++){
			this.testPerform();
		}
	}
	
	public void testPerform(){
		int count = 10;
		String exp = null;
		
		exp = "ognl";
		TimeProfileStack.push(exp);
		for(int i=0; i<count; i++){
			testGetOgnl();
		}
		TimeProfileStack.pop(exp);
		
		exp = "exp";
		TimeProfileStack.push(exp);
		for(int i=0; i<count; i++){
			testGetExp();
		}
		TimeProfileStack.pop(exp);
	}
	
	@Test
	public void testCopy(){
		Map map = new HashMap();
		map.put("userName", "namevalu");
		map.put("age", 111);
		map.put("name2", "name2");
		User user = new User();
		ReflectUtils.copy(map, user, true);
		Assert.assertEquals("namevalu", user.getUserName());
	}

	
	@Test
	public void testCopyWithConf(){
		Map map = new HashMap();
		map.put("userName", "namevalu");
		map.put("desc", "  ");
		map.put("age", 111);
		map.put("height", null);
		map.put("name2", "name2");
		
		User user = new User();
		user.setHeight(11);
		
		ReflectUtils.copy(map, user, CopyConfig.create());
		Assert.assertEquals("namevalu", user.getUserName());
		Assert.assertEquals("  ", user.getDesc());
		Assert.assertEquals(111, user.getAge());
		Assert.assertTrue(user.getHeight()==null);


		user = new User();
		user.setHeight(11);
//		ReflectUtils.copy(map, user, CopyConfig.create().ignoreNull().ignoreBlank().ignoreFields("age"));
		ReflectUtils.copyIgnoreBlank(map, user, "age");
		Assert.assertEquals("namevalu", user.getUserName());
		Assert.assertEquals(0, user.getAge());
		Assert.assertEquals(Integer.valueOf(11), user.getHeight());
		Assert.assertTrue(user.getDesc()==null);

		user = new User();
		user.setHeight(11);
		ReflectUtils.copy(map, user, CopyConfig.create().ignoreNull().ignoreBlank().includeFields("age"));
		Assert.assertNull(user.getUserName());
		Assert.assertEquals(111, user.getAge());
		Assert.assertEquals(Integer.valueOf(11), user.getHeight());
		Assert.assertNull(user.getDesc());
	}
	
	public static class UserSeperator {
		private String user_name;
		private Date birth_date;
		private String desc;
		private Integer height;
	}
	@Test
	public void testCopyFields(){
		UserSeperator map = new UserSeperator();
		map.user_name = "namevalu";
		map.birth_date = new Date();
		map.desc = "des";
		User user = new User();
		
		Integer h = 11;
		user.setHeight(h);
		ReflectUtils.copyFields(map, user, '_', true);
		Assert.assertEquals(map.user_name, user.getUserName());
		Assert.assertEquals(map.birth_date, user.getBirthDate());
		Assert.assertEquals(map.desc, user.getDesc());
		Assert.assertEquals(h, user.getHeight());
		

		ReflectUtils.copyFields(map, user, '_', false);
		Assert.assertNull(user.getHeight());
	}
	
	@Test
	public void testSetFieldValueByClass(){
		User user = new User();
		ReflectUtils.setFieldsDefaultValue(user, Integer.class, 163, int.class, 28);
		Assert.assertEquals(163, user.getHeight().longValue());
		Assert.assertEquals(163, user.getBust().longValue());
		Assert.assertEquals(28, user.getAge());
		
//		System.out.println(user.getAddress().getDetail());
	}

}
