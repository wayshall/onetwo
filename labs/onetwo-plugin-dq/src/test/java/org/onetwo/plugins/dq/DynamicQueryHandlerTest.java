package org.onetwo.plugins.dq;

import org.junit.Test;
import org.onetwo.common.fish.test.NullJFishEntityManagerImpl;
import org.onetwo.common.utils.Page;

public class DynamicQueryHandlerTest {

	@Test
	public void testDqh(){
		DynamicQueryHandler dqh = new DynamicQueryHandler(new NullJFishEntityManagerImpl(), TestQueryInterface.class);
		TestQueryInterface i = (TestQueryInterface)dqh.getProxyObject();
		System.out.println("page: " + i.findPage(new Page(), "page"));
		System.out.println("list: " + i.findList("list"));
		System.out.println("one: " + i.findOne("one"));
		System.out.println("countUser: " + i.countUser("countUser"));
	}
	
}
