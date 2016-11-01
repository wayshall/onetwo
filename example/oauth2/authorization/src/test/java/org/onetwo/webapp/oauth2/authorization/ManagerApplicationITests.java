package org.onetwo.webapp.oauth2.authorization;

import org.junit.runner.RunWith;
import org.onetwo.ext.security.utils.LoginUserDetails;
import org.onetwo.test.boot.BootMvcWithSecurityBaseITest;
import org.onetwo.webapp.oauth2.authorization.AuthorizationStarter;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=AuthorizationStarter.class, webEnvironment=WebEnvironment.MOCK)
public class ManagerApplicationITests extends BootMvcWithSecurityBaseITest {
	
	protected LoginUserDetails login(){
		return login("root", "test");
	}

}
