package org.onetwo.plugins.dq;

import org.junit.Test;
import org.onetwo.common.fish.test.NullJFishEntityManagerImpl;
import org.onetwo.common.utils.Page;

public class DynamicQueryHandlerTest {

	@Test
	public void testDqh(){
		DynamicQueryHandler dqh = new DynamicQueryHandler(new NullJFishEntityManagerImpl(), null, TestQueryInterface.class);
		TestQueryInterface i = (TestQueryInterface)dqh.getQueryObject();
		System.out.println("page: " + i.findPage(new Page<TestBean>(), "page"));
		System.out.println("list: " + i.findList("list"));
		System.out.println("one: " + i.findOne("one"));
		System.out.println("countUser: " + i.countUser("countUser"));
	}
	
}
