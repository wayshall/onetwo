package org.onetwo.web;

import ognl.Ognl;

import org.junit.Test;
import org.onetwo.common.utils.Page;

import com.google.common.collect.Maps;
import com.opensymphony.xwork2.ognl.OgnlUtil;

public class OgnlTest {

	@Test
	public void test() throws Exception{
		Page<?> page = new Page<>();
		Ognl.setValue("pageSize", page, "10");
		System.out.println("pagesize:"+page.getPageSize());
		

		Ognl.setValue("pageSize", page, new String[]{"10", "11"});
		System.out.println("pagesize:"+page.getPageSize());
		
		OgnlUtil ognl = new OgnlUtil();
		ognl.setProperty("pageSize", "10", page, Maps.newHashMap());
		System.out.println("pagesize:"+page.getPageSize());
		ognl.setProperty("pageSize", new String[]{"10"}, page, Maps.newHashMap());
		ognl.setProperty("pageSize", new String[]{"10", "11"}, page, Maps.newHashMap());
	}
}
