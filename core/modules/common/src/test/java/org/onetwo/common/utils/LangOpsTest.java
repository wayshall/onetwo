package org.onetwo.common.utils;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class LangOpsTest {
	
	@Test
	public void testArrayToMap(){
		Object[] arrays = new Object[]{"key1", 1, "key2", "value2"};
		Map<String, Object> map = LangOps.arrayToMap(arrays);
		System.out.println("map:"+map);
		Assert.assertNotNull(map);
		Assert.assertEquals(Integer.valueOf(1), map.get("key1"));
		Assert.assertEquals("value2", map.get("key2"));
		
		arrays = new Object[]{"key1", 1, "key2", "value2", "key3"};
		map = LangOps.arrayToMap(arrays);
		System.out.println("map:"+map);
		Assert.assertTrue(map.size()==2);
		Assert.assertNotNull(map);
		Assert.assertEquals(Integer.valueOf(1), map.get("key1"));
		Assert.assertEquals("value2", map.get("key2"));
		Assert.assertFalse(map.containsKey("key3"));
	}

}
