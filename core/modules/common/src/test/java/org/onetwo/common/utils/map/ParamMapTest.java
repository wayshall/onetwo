package org.onetwo.common.utils.map;


import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.utils.VerySimpleStartMatcher;

public class ParamMapTest {
	
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
		ParamMap map1 = new ParamMap("aa=aa-value&bb=bb-value");
		Assert.assertEquals("aa-value", map1.getFirstValue("aa"));
		Assert.assertEquals("bb-value", map1.getFirstValue("bb"));
		
		ParamMap addMap = new ParamMap();
		addMap.putElement("dd", "dd-value");
		addMap.putElement("dd", "dd-value2");
		addMap.putElement("test-ee", "testee-value");
		addMap.putElement("test-dd", "testdd-value");
		
		ParamMap map2 = new ParamMap("aa=aa-value&bb=bb-value");
		map2.putElements("cc", new String[]{"cc-value", "cc2-value"});
		map2.addWithFilter(addMap, "test-");
		
		ParamMap map = map2.subtract(map1);
		System.out.println("testParams map:"+map.toParamString());
		String expected = "cc=cc-value&cc=cc2-value&dd=dd-value&dd=dd-value2&test-ee=testee-value&test-dd=testdd-value";
		Assert.assertEquals(expected, map.toParamString());
	}
	
	@Test
	public void testFilterParams(){
		ParamMap map1 = new ParamMap("aa=aa-value&bb=bb-value");
		Assert.assertEquals("aa-value", map1.getFirstValue("aa"));
		Assert.assertEquals("bb-value", map1.getFirstValue("bb"));
		
		ParamMap addMap = new ParamMap();
		addMap.putElement("dd", "dd-value");
		addMap.putElement("test-ee", "testee-value");
		addMap.putElement("test-dd", "testdd-value");
		addMap.putElement("aa-test-dd", "testdd-value");
		
		map1.addMapWithFilter(addMap, "*test*");
		System.out.println("testFilterParams map1:" + map1);
		Assert.assertEquals("{aa=[aa-value], bb=[bb-value], dd=[dd-value]}", map1.toString());
	}

	
	@Test
	public void testFilterSelf(){
		ParamMap map1 = new ParamMap("aa=aa-value&bb=bb-value");
		map1.putElement("dd", "dd-value");
		map1.putElement("test-ee", "testee-value");
		map1.putElement("test-dd", "testdd-value");
		map1.putElement("aa-test-dd", "testdd-value");
		
		map1.filter("*test*");
		System.out.println("map1:" + map1);
		Assert.assertEquals("{aa=[aa-value], bb=[bb-value], dd=[dd-value]}", map1.toString());
	}
	
	

}
