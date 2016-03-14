package org.onetwo.boot.core;

import org.junit.Test;
import org.onetwo.common.profiling.TimeCounter;
import org.onetwo.common.utils.LangOps;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

public class BCryptPasswordEncoderTest {
	
	@Test
	public void test(){
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

}
