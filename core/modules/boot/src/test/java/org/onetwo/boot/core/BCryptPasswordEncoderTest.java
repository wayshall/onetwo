package org.onetwo.boot.core;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPasswordEncoderTest {
	
	@Test
	public void test(){
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String str = encoder.encode("13333333333");
		System.out.println("str:" + str);
	}

}
