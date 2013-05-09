package org.onetwo.common.spring.web;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.spring.web.BaseController.UrlHelper;

public class UrlHelperTest {
	
	@Test
	public void testRestUrl(){
		TestUserController b = new TestUserController();

		UrlHelper um = b.getUrlMeta();
		System.out.println("v: " + um.getIndexView());
		Assert.assertEquals("/member/test-user", um.getListAction());
		Assert.assertEquals("/member/test-user-edit", um.getEditView());
		Assert.assertEquals("/member/test-user-list", um.getListView());
		Assert.assertEquals("/member/test-user", um.getIndexView());
		Assert.assertEquals("/member/test-user-new", um.getNewView());
		Assert.assertEquals("/member/test-user/1/edit", um.editAction(1));
		Assert.assertEquals("/member/test-user", um.getListAction());
		Assert.assertEquals("/member/test-user/1", um.showAction(1));
	}

}
