package org.onetwo.test.boot;

import org.junit.Before;
import org.onetwo.ext.security.utils.LoginUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class BootMvcWithSecurityBaseTest extends BootMvcBaseTest {

	
	@Before
	public void initMockMvc(){
		this.mockMvcs = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.apply(SecurityMockMvcConfigurers.springSecurity())
				.build();
	}
	

	protected LoginUserDetails mockLogin(String loginUrl, String userName, String password){
		return SecurityTestUtils.mockLogin(mockMvcs, loginUrl, userName, password);
	}
}
