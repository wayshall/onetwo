package org.onetwo.boot.test;

import org.onetwo.boot.module.security.BootSecurityConfig;
import org.onetwo.ext.security.utils.GenericLoginUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class BootMvcWithSecurityBaseITest extends BootMvcBaseITest {

	static private GenericLoginUserDetails<?> loginUser;
	@Autowired
	protected BootSecurityConfig bootSecurityConfig;
	
	
	public static GenericLoginUserDetails<?> currentLoginUser() {
		return loginUser;
	}

	@Override
	public MockMvc buildMockMvc(WebApplicationContext webApplicationContext){
		return MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.apply(SecurityMockMvcConfigurers.springSecurity())
				.build();
	}
	
	protected GenericLoginUserDetails<?> mockLogin(String loginUrl, String userName, String password){
		return SecurityTestUtils.mockLogin(mockMvc(), loginUrl, userName, password);
	}
	
	
	protected GenericLoginUserDetails<?> login(String userName, String password){
		if(loginUser==null){
			loginUser = mockLogin(bootSecurityConfig.getLoginProcessUrl(), "root", "test");
		}
		return loginUser;
	}
}
