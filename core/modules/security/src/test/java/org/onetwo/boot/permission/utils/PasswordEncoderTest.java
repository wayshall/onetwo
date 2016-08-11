package org.onetwo.boot.permission.utils;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.profiling.TimeCounter;
import org.onetwo.common.utils.LangOps;
import org.springframework.security.authentication.encoding.LdapShaPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

public class PasswordEncoderTest {
	
	@Test
	public void testBcrypt(){
		String pwd = "13333333333";
		StandardPasswordEncoder def = new StandardPasswordEncoder();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		TimeCounter time = new TimeCounter("sha");
		int times = 1;
		time.start();
		LangOps.repeatRun(times, ()->{
			def.encode(pwd);
		});
		time.stop();
		
		time.restart("bcrypt");
		LangOps.repeatRun(times, ()->{
			String str = encoder.encode(pwd);
			System.out.println(str.length()+":"+str);
		});
		time.stop();
	}
	
	@Test
	public void testSsha(){
		LdapShaPasswordEncoder encoder = new LdapShaPasswordEncoder();
		
		String rawPass = "test";
		String salt = "@#%AS%&DF_=PJ}{EB23+42342*()*^%$)_(*%^)";
		String res = encoder.encodePassword(rawPass, salt.getBytes());
		System.out.println("res:"+res);
		boolean valid = encoder.isPasswordValid(res, rawPass, "aasd@#$@3");
		Assert.assertTrue(valid);
	}

}
