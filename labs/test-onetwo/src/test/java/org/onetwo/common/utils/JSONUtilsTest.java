package org.onetwo.common.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.utils.User.Address;
import org.onetwo.common.utils.json.JSONUtils;

public class JSONUtilsTest {

//	@Test
	public void testUser(){
		String dateStr = "2011-07-8";
		Date date = DateUtil.parse(dateStr, DateUtil.YYYY_MM_DD);
		String json = "{userName:'wayshall', age:18, birthDate:'"+dateStr+"'}";
		User user = JSONUtils.getObject(json, User.class);
		Assert.assertEquals("wayshall", user.getUserName());
		Assert.assertEquals(date.getTime(), user.getBirthDate().getTime());
		String str = JSONUtils.getJsonString(user);
		System.out.println(str);
	}
	
	@Test
	public void testInclude(){
		User user = new User();
		user.setUserName("wayshall");
		user.setAge(27);
		user.setBirthDate(DateUtil.now());
		Address adr = new Address();
		adr.setPhone("123234234");
		adr.setFax("2354q3454");
		adr.setDetail("detail ");
		user.setAddress(adr);
		
		Map map = new HashMap();
		map.put("user", user);
		map.put("mapName", "testMap");
		
		String str = JSONUtils.getJsonString(map, true, "user", "userName", "age");
		System.out.println("with: " + str);
		Assert.assertTrue(str.indexOf("\"userName\"")!=-1);
		Assert.assertTrue(str.indexOf("\"age\"")!=-1);
		
		str = JSONUtils.getJsonString(map, false, "userName", "age");
		System.out.println("exclude: " + str);
		Assert.assertTrue(str.indexOf("\"userName\"")==-1);
		Assert.assertTrue(str.indexOf("\"age\"")==-1);
	}
}
