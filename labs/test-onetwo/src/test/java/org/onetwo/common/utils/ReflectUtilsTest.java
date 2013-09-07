package org.onetwo.common.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Id;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.profiling.UtilTimerStack;

import test.entity.UserEntity;

public class ReflectUtilsTest {
	

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
		Map map = ReflectUtils.toMap(user);
		Assert.assertEquals("{avaiable=false, age=11, userName=testName, birthDate=Thu Apr 18 14:00:00 CST 2013}", map.toString());
		map = ReflectUtils.toMap(false, user);
		Assert.assertEquals("{avaiable=false, height=null, desc=null, bust=null, address=null, age=11, userName=testName, birthDate=Thu Apr 18 14:00:00 CST 2013, address2=null}", map.toString());
		Map map2 = ReflectUtils.field2Map(user);
		System.out.println("map2: " + map2);
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
		UtilTimerStack.setActive(true);
		int count = 4;
		for(int i=0; i<count; i++){
			this.testPerform();
		}
	}
	
	public void testPerform(){
		int count = 10;
		String exp = null;
		
		exp = "ognl";
		UtilTimerStack.push(exp);
		for(int i=0; i<count; i++){
			testGetOgnl();
		}
		UtilTimerStack.pop(exp);
		
		exp = "exp";
		UtilTimerStack.push(exp);
		for(int i=0; i<count; i++){
			testGetExp();
		}
		UtilTimerStack.pop(exp);
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
		ReflectUtils.copy(map, user, CopyConfig.create().ignoreNull().ignoreBlank().ignoreFields("age"));
		Assert.assertEquals("namevalu", user.getUserName());
		Assert.assertEquals(0, user.getAge());
		Assert.assertEquals(Integer.valueOf(11), user.getHeight());
		Assert.assertTrue(user.getDesc()==null);
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
