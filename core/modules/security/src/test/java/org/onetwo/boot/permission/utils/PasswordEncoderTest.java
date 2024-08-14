package org.onetwo.boot.permission.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.security.SecureRandom;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.profiling.TimeCounter;
import org.onetwo.common.utils.LangOps;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

public class PasswordEncoderTest {
	@Test
	public void testBcryptEncodePassword(){
		String pwd = "test";
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String result = encoder.encode(pwd);
		System.out.println("result: " + result);
		assertThat(encoder.matches(pwd, result));
	}
	
	@Test
	public void testBcrypt(){
		String pwd = "test";
		String salt = "keys$-_^测";
		byte[] saltbytes = salt.getBytes();
		int strength = 4; //指定SecureRandom后，只要符合范围，没什么用
		BCryptPasswordEncoder saltEncoder = new BCryptPasswordEncoder(strength, new SecureRandom(saltbytes));
		
		String result = saltEncoder.encode(pwd);
		assertThat(saltEncoder.matches(pwd, result));
		System.out.println("slat result: " + result);

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		result = encoder.encode(pwd);
		System.out.println("BCryptPasswordEncoder result1: " + result);
		assertThat(saltEncoder.matches(pwd, result));
		result = encoder.encode(pwd);
		System.out.println("BCryptPasswordEncoder result2: " + result);

		result = saltEncoder.encode(pwd);
		System.out.println("result: " + result);
		assertThat(encoder.matches(pwd, result));
	}
	
	@Test
	public void testBcrypt2(){
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
