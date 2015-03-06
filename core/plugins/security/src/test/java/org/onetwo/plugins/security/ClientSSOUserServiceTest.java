package org.onetwo.plugins.security;

import javax.annotation.Resource;

import org.junit.Test;
import org.onetwo.common.sso.CurrentLoginUserParams;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.sso.SSOUserService;
import org.onetwo.plugins.security.ClientSSOUserServiceLoader.ClientSSOUserServiceContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(classes=ClientSSOUserServiceContext.class)
public class ClientSSOUserServiceTest extends AbstractJUnit4SpringContextTests {
	
	@Resource
	private SSOUserService ssoUserServiceProxy;

	@Test
	public void test(){
		CurrentLoginUserParams params = new CurrentLoginUserParams("test");
		UserDetail user = this.ssoUserServiceProxy.getCurrentLoginUser(params);
		System.out.println("user: " + user);
	}
}
