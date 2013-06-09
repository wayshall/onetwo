package org.onetwo.plugins.dq;

import org.junit.Test;

public class DynamicQueryHandlerTest {

	@Test
	public void testDqh(){
		DynamicQueryHandler dqh = new DynamicQueryHandler(null, TestQueryInterface.class);
		TestQueryInterface i = (TestQueryInterface)dqh.getProxyObject();
		System.out.println("page: " + i.findPage("page"));
		System.out.println("list: " + i.findList("list"));
		System.out.println("one: " + i.findOne("one"));
	}
	
}
