package org.onetwo.common.utils.list;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.utils.VerySimpleStartMatcher;
import org.onetwo.common.utils.map.CasualMap;

public class CasualMapTest {
	
	@Test
	public void testVerySimpleStartMatcher(){
		String start = "testaaaaaaaaaa";
		String end = "aaaaaaaaaaatest";
		String all = "aaaaaaaaatestbbbbbbbbb";
		VerySimpleStartMatcher mstart = VerySimpleStartMatcher.create("test*");
		Assert.assertTrue(mstart.match(start));
		Assert.assertFalse(mstart.match(end));
		Assert.assertFalse(mstart.match(all));

		VerySimpleStartMatcher mend = VerySimpleStartMatcher.create("*test");
		Assert.assertTrue(mend.match(end));
		Assert.assertFalse(mend.match(start));
		Assert.assertFalse(mend.match(all));

		VerySimpleStartMatcher mall = VerySimpleStartMatcher.create("*test*");
		Assert.assertTrue(mall.match(all));
		Assert.assertTrue(mall.match(start));
		Assert.assertTrue(mall.match(end));
	}
	
	@Test
	public void testParams(){
		CasualMap map1 = new CasualMap("aa=aa-value&bb=bb-value");
		Assert.assertEquals("aa-value", map1.get("aa"));
		Assert.assertEquals("bb-value", map1.get("bb"));
		
		CasualMap addMap = new CasualMap();
		addMap.put("dd", "dd-value");
		addMap.put("dd", "dd-value2");
		addMap.put("test-ee", "testee-value");
		addMap.put("test-dd", "testdd-value");
		
		CasualMap map2 = new CasualMap("aa=aa-value&bb=bb-value");
		map2.put("cc", new String[]{"cc-value", "cc2-value"});
		map2.addWithout(addMap, "test-");
		
		CasualMap map = map2.subtract(map1);
		System.out.println("map:"+map.toParamString());
		String expected = "cc=cc-value&cc=cc2-value&dd=dd-value&dd=dd-value2&test-ee=testee-value&test-dd=testdd-value";
		Assert.assertEquals(expected, map.toParamString());
	}
	
	@Test
	public void testFilterParams(){
		CasualMap map1 = new CasualMap("aa=aa-value&bb=bb-value");
		Assert.assertEquals("aa-value", map1.get("aa"));
		Assert.assertEquals("bb-value", map1.get("bb"));
		
		CasualMap addMap = new CasualMap();
		addMap.put("dd", "dd-value");
		addMap.put("test-ee", "testee-value");
		addMap.put("test-dd", "testdd-value");
		addMap.put("aa-test-dd", "testdd-value");
		
		map1.addHttpParameterWithout(addMap, "*test*");
		System.out.println("map1:" + map1);
		Assert.assertEquals("{aa=aa-value, bb=bb-value, dd=dd-value}", map1.toString());
	}

	
	@Test
	public void testFilterSelf(){
		CasualMap map1 = new CasualMap("aa=aa-value&bb=bb-value");
		map1.put("dd", "dd-value");
		map1.put("test-ee", "testee-value");
		map1.put("test-dd", "testdd-value");
		map1.put("aa-test-dd", "testdd-value");
		
		map1.filter("*test*");
		System.out.println("map1:" + map1);
		Assert.assertEquals("{aa=aa-value, bb=bb-value, dd=dd-value}", map1.toString());
	}
	
	

}
