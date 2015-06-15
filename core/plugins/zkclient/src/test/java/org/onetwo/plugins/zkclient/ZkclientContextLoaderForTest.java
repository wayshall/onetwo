package org.onetwo.plugins.zkclient;


import org.onetwo.common.test.spring.SpringConfigApplicationContextLoader;
import org.springframework.context.annotation.Bean;



public class ZkclientContextLoaderForTest extends SpringConfigApplicationContextLoader {
	

	@Override
	protected Class<?>[] getClassArray() {
		return new Class<?>[]{ZkclientPluginModelContextTest.class};
	}

	public static class ZkclientPluginModelContextTest {
		@Bean
		public ZkclientSiteConfigTest zkclientSiteConfigTest(){
			return new ZkclientSiteConfigTest();
		}
	}
	
}
