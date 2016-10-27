package org.onetwo.common.utils.map;

import java.util.Comparator;

import org.junit.Assert;
import org.junit.Test;

public class HttpParamsMapTest {
	
	@Test
	public void test(){
		ParamsMap map = new ParamsMap();
		map.put("bb", "bb1");
		map.put("cc", "cc1");
		map.put("aa", "aa1");
		map.put("aa", "aa2");
		map.put("dd", "dd1");
		System.out.println("map:"+map);
		String pstr = map.toParamString();
		System.out.println("pstr:"+pstr);
		Assert.assertEquals("bb=bb1&cc=cc1&aa=aa1&aa=aa2&dd=dd1", pstr);
		pstr = map.toParamString(Comparator.comparing(e->e));
		System.out.println("pstr:"+pstr);
		Assert.assertEquals("aa=aa1&aa=aa2&bb=bb1&cc=cc1&dd=dd1", pstr);
	}

}
