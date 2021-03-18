package org.onetwo.common.apiclient;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.onetwo.common.apiclient.api.simple.SwaggerClient;
import org.onetwo.common.apiclient.api.simple.SwaggerClient.SwaggerResponse;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wayshall
 * <br/>
 */
public class SwaggerClientTest extends ApiClientBaseTests {
	
	@Autowired
	SwaggerClient swaggerClient;
	
	@Test
	public void testGetGroups(){
		List<SwaggerResponse> res = swaggerClient.getGroups();
		assertThat(res).isNotNull();

		System.out.println("res： " + res);
		res.forEach(sw->{
			System.out.println("name： " + sw.getName());
		});
	}

}
