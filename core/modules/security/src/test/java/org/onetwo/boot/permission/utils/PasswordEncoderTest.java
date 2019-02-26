package org.onetwo.boot.permission.utils;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.profiling.TimeCounter;
import org.onetwo.common.utils.LangOps;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

public class PasswordEncoderTest {
	
	@Test
	public void testBcrypt(){
		String pwd = "jfish";
		StandardPasswordEncoder def = new StandardPasswordEncoder();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		TimeCounter time = new TimeCounter("sha");
		int times = 1;
		time.start();
		LangOps.ntimesRun(times, ()->{
			def.encode(pwd);
		});
		time.stop();
		
		time.restart("bcrypt");
		LangOps.ntimesRun(times, ()->{
			String str = encoder.encode(pwd);
			System.out.println("BCrypt "+str.length()+":"+str);
		});
		time.stop();
	}
	
	@Test
	public void testSsha(){
		LdapShaPasswordEncoder encoder = new LdapShaPasswordEncoder();
		
		String rawPass = "test";
		String salt = "@#%AS%&DF_=PJ}{EB23+42342*()*^%$)_(*%^)";
		String res = encoder.encode(rawPass);
		System.out.println("res:"+res);
		boolean valid = encoder.matches(rawPass, res);
		Assert.assertTrue(valid);
	}

}
