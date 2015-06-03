package org.onetwo.plugins.jsonrpc.client;


import org.onetwo.common.test.spring.SpringConfigApplicationContextLoader;
import org.springframework.context.annotation.Bean;



public class JsonRpcClientContextLoaderForTest extends SpringConfigApplicationContextLoader {
	

	@Override
	protected Class<?>[] getClassArray() {
		return new Class<?>[]{ZkclientPluginModelContextTest.class};
	}

	public static class ZkclientPluginModelContextTest {
		@Bean
		public JsonRpcClientSiteConfigTest zkclientSiteConfigTest(){
			return new JsonRpcClientSiteConfigTest();
		}
	}
	
}
