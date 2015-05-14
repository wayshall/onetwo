package org.onetwo.plugins.test;

import org.junit.Test;
import org.onetwo.plugins.rest.RestClientHandler;
import org.onetwo.plugins.rest.annotation.RestClient;
import org.springframework.web.bind.annotation.RequestMethod;

public class RestClientTest {
	
	@RestClient(value="http://localhost:8080/test", method=RequestMethod.GET)
	public static interface TestRestInterface {
		String sayTo(String userName);
	}
	
	@Test
	public void testRest(){
		RestClientHandler client = new RestClientHandler(TestRestInterface.class);
	}

}
