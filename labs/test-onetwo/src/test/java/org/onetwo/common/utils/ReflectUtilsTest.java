package org.onetwo.common.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.opensymphony.xwork2.util.profiling.UtilTimerStack;

public class ReflectUtilsTest {
	
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
		System.out.println("username: " + user.getUserName());
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
