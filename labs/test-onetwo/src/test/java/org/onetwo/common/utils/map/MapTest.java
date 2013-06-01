package org.onetwo.common.utils.map;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.User;

public class MapTest {

	@Test
	public void testFilterNull(){
		Map map = M.c("test", "testVAlue", "testnull", null, "testempty", "");
		M.filterNull(map);
		System.out.println(map);
		Assert.assertEquals(2, map.size());
	}

	@Test
	public void testFilterEmpty(){
		Map map = M.c("test", "testVAlue", "testnull", null, "testempty", "");
		M.filterBlank(map);
		System.out.println(map);
		Assert.assertEquals(1, map.size());
	}
	
	@Test
	public void testObject2Map(){
		User obj = new User();
		String userName = "map";
		obj.setUserName(userName);
		obj.setDesc("");
		Map map = null;

		map = M.bean2Map(obj);
		LangUtils.println("map:", map);
		Assert.assertTrue(map.containsKey("userName"));
		Assert.assertEquals(map.get("userName"), obj.getUserName());
		
		map = M.bean2Map(obj, ":ignore", ":null");
		LangUtils.println("map:", map);
		Assert.assertTrue(map.containsKey("userName"));
		
		Assert.assertTrue(map.containsKey("desc"));
		Assert.assertFalse(map.containsKey("birthDate"));
		Assert.assertFalse(map.containsKey("height"));
		Assert.assertFalse(map.containsKey("bust"));
		Assert.assertEquals(map.get("userName"), obj.getUserName());
		
		map = M.bean2Map(obj, ":ignore", ":empty");
		LangUtils.println("map:", map);
		Assert.assertTrue(map.containsKey("userName"));
		Assert.assertFalse(map.containsKey("desc"));
		
		Assert.assertFalse(map.containsKey("birthDate"));
		Assert.assertFalse(map.containsKey("height"));
		Assert.assertFalse(map.containsKey("bust"));
		Assert.assertEquals(map.get("userName"), obj.getUserName());
		
		map = M.bean2Map(obj, ":ignore", ":empty", Integer.class, 0);
		LangUtils.println("map:", map);
		Assert.assertTrue(map.containsKey("userName"));

		Assert.assertFalse(map.containsKey("desc"));
		Assert.assertFalse(map.containsKey("age"));
		Assert.assertFalse(map.containsKey("birthDate"));
		Assert.assertFalse(map.containsKey("height"));
		Assert.assertFalse(map.containsKey("bust"));
		Assert.assertEquals(map.get("userName"), obj.getUserName());
		
		map = M.bean2Map(obj, ":ignore", ":empty", Integer.class, 0, "userName", userName);
		LangUtils.println("map:", map);
		Assert.assertFalse(map.containsKey("userName"));

		Assert.assertFalse(map.containsKey("desc"));
		Assert.assertFalse(map.containsKey("age"));
		Assert.assertFalse(map.containsKey("birthDate"));
		Assert.assertFalse(map.containsKey("height"));
		Assert.assertFalse(map.containsKey("bust"));
	}
}
