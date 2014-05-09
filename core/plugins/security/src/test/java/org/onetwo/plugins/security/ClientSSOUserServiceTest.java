package org.onetwo.plugins.security;

import javax.annotation.Resource;

import org.junit.Test;
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
		UserDetail user = this.ssoUserServiceProxy.getCurrentLoginUserByToken("test");
		System.out.println("user: " + user);
	}
}
