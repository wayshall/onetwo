package org.onetwo.common.spring;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

public class ClassPathResouceTest {
	
	@Test
	public void test(){
		String path = Thread.currentThread().getContextClassLoader().getResource("").getFile();
		System.out.println("path:"+path);
		ClassPathResource cpr = new ClassPathResource("test.txt");
		System.out.println(cpr.getPath());
	}

}
