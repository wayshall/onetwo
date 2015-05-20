package org.onetwo.plugins.test;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.onetwo.plugins.rest.RestClientFacotry;
import org.onetwo.plugins.rest.annotation.RestClient;
import org.springframework.web.bind.annotation.RequestMethod;

public class RestClientTest {
	
	@RestClient(value="http://localhost:8080/appweb-admin/test", method=RequestMethod.GET)
	public static interface TestRestInterface {
		String sayTo(String userName);
		
		@RestClient(value="work", method=RequestMethod.GET)
		public Map<?, ?> work(String userName, String workCity);
	}
	
	@Test
	public void testRestStringWithSayTo(){
		TestRestInterface client = RestClientFacotry.create(TestRestInterface.class);
		String rs = client.sayTo("world");
		Assert.assertEquals("Hello world", rs);
	}
	
	@Test
	public void testRestMapWithWork(){
		TestRestInterface client = RestClientFacotry.create(TestRestInterface.class);
		Map<?, ?> rsMap = client.work("test", "gz");
		Assert.assertEquals("{workCity=gz, userName=test}", rsMap.toString());
	}

}
