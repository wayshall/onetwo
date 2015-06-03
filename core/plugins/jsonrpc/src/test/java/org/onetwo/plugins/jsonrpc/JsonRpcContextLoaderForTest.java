package org.onetwo.plugins.jsonrpc;


import org.onetwo.common.test.spring.SpringConfigApplicationContextLoader;
import org.springframework.context.annotation.Bean;



public class JsonRpcContextLoaderForTest extends SpringConfigApplicationContextLoader {
	

	@Override
	protected Class<?>[] getClassArray() {
		return new Class<?>[]{ZkclientPluginModelContextTest.class};
	}

	public static class ZkclientPluginModelContextTest {
		@Bean
		public JsonRpcSiteConfigTest zkclientSiteConfigTest(){
			return new JsonRpcSiteConfigTest();
		}
	}
	
}
