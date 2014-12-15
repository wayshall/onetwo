package org.onetwo.plugins.groovy;

import org.onetwo.common.test.spring.SpringConfigApplicationContextLoader;
import org.onetwo.plugins.groovy.model.JavaServiceImpl;
import org.springframework.context.annotation.Bean;



public class GroovyContextLoaderForTest extends SpringConfigApplicationContextLoader {
	

	@Override
	protected Class<?>[] getClassArray() {
		return new Class<?>[]{GroovyPluginModelContextTest.class};
	}

	public static class GroovyPluginModelContextTest {
		@Bean
		public JavaServiceImpl javaServiceImpl(){
			return new JavaServiceImpl();
		}
	}
	
}
