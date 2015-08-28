package org.onetwo.boot.test;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;

import com.jayway.restassured.RestAssured;

@IntegrationTest("server.port:0")
public class RestAssuredBaseTests {

	@Value("${local.server.port}")
	protected int serverPort;
	
	@Before
	public void setupRest(){
		RestAssured.port = serverPort;
	}
}
