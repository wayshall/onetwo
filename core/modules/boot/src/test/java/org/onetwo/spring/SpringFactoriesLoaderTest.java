package org.onetwo.spring;

import java.util.List;

import org.junit.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.ClassUtils;

public class SpringFactoriesLoaderTest {
	
	@Test
	public void test(){
		List<String> names = SpringFactoriesLoader.loadFactoryNames(EnableAutoConfiguration.class, ClassUtils.getDefaultClassLoader());
		System.out.println("names:"+names);
	}

}
